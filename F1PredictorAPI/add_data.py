from datetime import datetime
from operator import indexOf
from pickle import GLOBAL

from attr.validators import instance_of
from pydantic import constr

from db_connection import get_engine, get_session
from models import Base, Constructor, Driver, Circuit, GrandPrix, Result
import logging
from fast_f1_api import *

logging.basicConfig(level=logging.WARNING)
logging.getLogger('sqlalchemy.engine').setLevel(logging.WARNING)
logging.getLogger('fastf1').setLevel(logging.WARNING)

season = 2022

session = None

def initialize_database():
    engine = get_engine()
    Base.metadata.create_all(engine)

def start():
    global session
    initialize_database()
    session = get_session()

def get_relevant_drivers(db_drivers, session_drivers):
    relevant_drivers = []
    for driver in db_drivers:
        for (session_driver) in session_drivers:
            if driver.short_name == session_driver.__getitem__("ShortName"):
                relevant_drivers.append(driver)
                break
    return relevant_drivers

def get_avg_weather_data(weather_info):
    avg_weather = {
        'AirTemp': 0,
        'TrackTemp': 0,
        'Humidity': 0,
        'Rainfall': False,
        'WindSpeed': 0
    }
    for data_package in weather_info:
        avg_weather['AirTemp'] += data_package['AirTemp']/len(weather_info)
        avg_weather['TrackTemp'] += data_package['TrackTemp']/len(weather_info)
        avg_weather['Humidity'] += data_package['Humidity']/len(weather_info)
        if data_package['Rainfall']:
            avg_weather['Rainfall'] = True
        avg_weather['WindSpeed'] += data_package['WindSpeed']/len(weather_info)

    return avg_weather

def confirm_and_add(new_object, confirm=True):
    if (not confirm) or ('Y' == input(f"Confirm addition of new object {new_object}: (Y/n) - ").upper()):
        session.add(new_object)
        session.commit()
        print("Object added to db.")
    else:
        print("Object not added.")

def calculate_flags(flag_type, round):
    flag_type = flag_type.upper()

    session_race_control = get_session_race_control(season, round)

    number_of_flags = 0
    for index, message in session_race_control.iterrows():
        # Access data in the row as a dictionary-like structure
        if message['Category'] == 'Flag' and message['Flag'] == flag_type:
            number_of_flags += 1

    print(f"Number of {flag_type} flags: {number_of_flags}")
    return number_of_flags

def get_position(driver, race_results):
    for result in race_results:
        if result['Abbreviation'] == driver.short_name:
            # Found the driver
            if result['ClassifiedPosition'] in ['R', 'D', 'E', 'W', 'F', 'N']:
                return 0
            return result['Position']
    print(f"Driver {driver.full_name} ({driver.car_number}) not found in race results.")
    return -1

def get_points(driver, race_results):
    for result in race_results:
        if result['Abbreviation'] == driver.short_name:
            # Found the driver
            return result['Points']
    print(f"Driver {driver.full_name} ({driver.car_number}) not found in race results.")
    return 0

def get_fastest_lap_driver(race_results):
    points = [25, 18, 15, 12, 10, 8, 6, 4, 2, 1]
    for result in race_results:
        if result['Points'] > 0 and result['Points'] not in points:
            print(f"Fastest lap driver: {result['Abbreviation']} with {result['Points']} points.")
            return session.query(Driver).filter(Driver.short_name == result['Abbreviation']).first()

    # Maybe add a check for the fastest lap driver in the session data (lap by lap)?!

def get_current_constructor(driver, session_drivers):
    for session_driver in session_drivers:
        if session_driver.__getitem__('ShortName') == driver.short_name:
            constructor = session.query(Constructor).filter(Constructor.full_name == session_driver.__getitem__('ConstructorName')).first()
            if constructor is not None:
                return constructor
            print(f"Could not find constructor {session_driver.__getitem__('ConstructorName')}.")
    return None

def get_constructor_full_name(driver, session_drivers):
    for session_driver in session_drivers:
        if session_driver.__getitem__('ShortName') == driver.short_name:
            return session_driver.__getitem__('ConstructorName')
    return None

def main(skip):
    start_time = datetime.now()
    start()

    print("Fetching number of rounds...")
    number_of_rounds = int(get_season_rounds(season))
    db_drivers = session.query(Driver).all()

    for (round) in range(1, number_of_rounds):

        if skip and (session.query(GrandPrix).filter(GrandPrix.season == season, GrandPrix.round == round).first()) is not None:
            print(f"Round {round}-{season} already exists in database. Skipping...")
            continue
        else:
            print(f"Processing round {round}-{season} of {number_of_rounds} rounds")

        # Get data from FastF1 API
        print("Fetching session info...")
        session_info = get_session_info(season, round, "R")
        print(session_info)
        if session_info is None:
            print(f"No session info found for round {round}-{season}. Skipping...")
            continue

        # Check if circuit exists in database. If not, add it
        circuit = session.query(Circuit).filter(Circuit.location_name == session_info.__getitem__('Location')).first()
        if circuit is None:
            if session_info.__getitem__('Location') == "Monaco":
                circuit = session.query(Circuit).filter(Circuit.full_name == "Circuit de Monaco").first()
            elif session_info.__getitem__('Location') == "Singapore":
                circuit = session.query(Circuit).filter(Circuit.full_name == "Marina Bay Circuit").first()
            elif session_info.__getitem__('Location') == "Yas Marina":
                circuit = session.query(Circuit).filter(Circuit.full_name == "Yas Marina Circuit").first()
            else:
                print("\n### NEW Circuit ###")
                circuit = Circuit(
                    full_name=input(f"Enter Circuit Name ({session_info.__getitem__('EventName')}, {session_info.__getitem__('Location')}):\n"),
                    location_name=session_info.__getitem__('Location'),
                    country=session_info.__getitem__('Country'),
                    timezone=str(session_info.__getitem__('Session5Date').tzinfo),
                    length=float(input(f"Enter Circuit Length:\n")),
                    corners=session_info.__getitem__('Corners'),
                    laps=session_info.__getitem__('Laps'),
                    drs_zones= 0 # Can calculate this from race control message data
                )
                confirm_and_add(circuit)

        # Check if grand prix exists in database. If not, add it
        grand_prix = session.query(GrandPrix).filter(GrandPrix.season == season, GrandPrix.round == round).first()
        if grand_prix is None:
            print(f"\n### NEW Grand Prix ### For circuit {circuit.full_name} ({circuit.circuit_id}). Fetching weather data...")
            session_weather = get_avg_weather_data(get_weather_data(season, round, "R"))
            grand_prix = GrandPrix(
                circuit_id=circuit.circuit_id,
                full_name=session_info.__getitem__('EventName'),
                season=season,
                round=round,
                start_time=session_info.__getitem__('Session5Date').tz_convert('UTC').to_pydatetime(),
                sprint=session_info.__getitem__('EventFormat')!='conventional',
                red_flags=calculate_flags('red', round),
                yellow_flags=calculate_flags('yellow', round),
                air_temperature=session_weather.__getitem__('AirTemp'),
                track_temperature=session_weather.__getitem__('TrackTemp'),
                humidity=session_weather.__getitem__('Humidity'),
                wind_speed=session_weather.__getitem__('WindSpeed'),
                rainfall=session_weather.__getitem__('Rainfall')
            )
            confirm_and_add(grand_prix, False)

        print("Fetching session drivers...")
        session_drivers = get_session_drivers(season, round, "R")
        print(session_drivers)
        # Relevant drivers are the drivers that are in the database AND are in the session
        relevant_drivers = get_relevant_drivers(db_drivers, session_drivers)

        print(f"\nRound {round} ({session_info.__getitem__('EventName')}) [{session_info.__getitem__('EventFormat')}] with {len(relevant_drivers)}/{len(db_drivers)} drivers")

        print("Fetching race results...")
        race_results = get_results(season, round, 'R')
        print(race_results)

        print("Fetching qualifying results...")
        qualifying_results = get_results(season, round, 'Q')
        print(qualifying_results)

        sprint_results = None
        if grand_prix.sprint:
            print("Fetching sprint results...")
            sprint_results = get_results(season, round, 'S')
            print(sprint_results)
        else:
            print("--- No sprint results for this round.")

        print("### Adding driver results ###")
        for (driver) in relevant_drivers:

            current_constructor = get_current_constructor(driver, session_drivers)
            if current_constructor is None:
                constructor_full_name = get_constructor_full_name(driver, session_drivers)
                print(f"### NEW CONSTRUCTOR ### for driver {driver.full_name}. With name {constructor_full_name}")
                current_constructor = Constructor(
                    full_name=constructor_full_name,
                    short_name=input("Enter Constructor Short Name:\n"),
                    country=input("Enter Constructor Country:\n"),
                    active=False,
                    fantasy_points=0,
                    fantasy_price=0.0
                )
                confirm_and_add(current_constructor, True)

            if grand_prix.sprint:
                sprint_position = get_position(driver, sprint_results)
                if sprint_position != -1:
                    sprint_result = session.query(Result).filter(Result.driver_id == driver.driver_id,
                                                                 Result.constructor_id == driver.constructor_id,
                                                                 Result.grand_prix_id == grand_prix.grand_prix_id,
                                                                 Result.session_type == 'S').first()
                    if sprint_result is None:
                        print(f"Adding sprint result for {driver.full_name} ({driver.car_number}).")
                        sprint_result = Result(
                            driver_id=driver.driver_id,
                            constructor_id=current_constructor.constructor_id,
                            grand_prix_id=grand_prix.grand_prix_id,
                            position=sprint_position,
                            points=get_points(driver, sprint_results),
                            session_type='S',
                        )
                        confirm_and_add(sprint_result, False)
                    else:
                        print(f"Sprint result for {driver.full_name} ({driver.car_number}) already exists. Skipping...")

            qualifying_position = get_position(driver, qualifying_results)
            if qualifying_position != -1:
                qualifying_result = session.query(Result).filter(Result.driver_id == driver.driver_id,
                                                                 Result.constructor_id == driver.constructor_id,
                                                                 Result.grand_prix_id == grand_prix.grand_prix_id,
                                                                 Result.session_type == 'Q').first()
                if qualifying_result is None:
                    print(f"Adding qualifying result for {driver.full_name} ({driver.car_number}).")
                    qualifying_result = Result(
                        driver_id=driver.driver_id,
                        constructor_id=current_constructor.constructor_id,
                        grand_prix_id=grand_prix.grand_prix_id,
                        position=get_position(driver, qualifying_results),
                        points=0, # Always 0 for qualifying
                        session_type='Q',
                    )
                    confirm_and_add(qualifying_result, False)
                else:
                    print(f"Qualifying result for {driver.full_name} ({driver.car_number}) already exists. Skipping...")

            race_position = get_position(driver, race_results)
            if race_position != -1:
                race_result = session.query(Result).filter(Result.driver_id == driver.driver_id,
                                                           Result.constructor_id == driver.constructor_id,
                                                           Result.grand_prix_id == grand_prix.grand_prix_id,
                                                           Result.session_type == 'R').first()
                if race_result is None:
                    print(f"Adding race result for {driver.full_name} ({driver.car_number}).")
                    race_result = Result(
                        driver_id=driver.driver_id,
                        constructor_id=current_constructor.constructor_id,
                        grand_prix_id=grand_prix.grand_prix_id,
                        position=race_position,
                        points=get_points(driver, race_results),
                        session_type='R',
                    )
                    confirm_and_add(race_result, False)
                else:
                    print(f"Race result for {driver.full_name} ({driver.car_number}) already exists. Skipping...")

    end_time = datetime.now()
    print(f"Execution time: {(end_time - start_time)}")

if __name__ == "__main__":
    skip = 'Y' == input(f"Do you want to skip already added grands prix? (Y/n) - ").upper()
    for i in range(2023, 2024, 1):
        season = i
        print(f"#### STARTING SEASON {season} ####")
        main(skip)
    session.close()