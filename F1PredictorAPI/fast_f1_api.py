import os
import shutil
import fastf1
from fastapi import FastAPI, HTTPException
from fastapi.routing import APIRoute
from fastf1.core import NoLapDataError
from fastapi.responses import JSONResponse
from fastapi.encoders import jsonable_encoder
from fastapi.middleware.cors import CORSMiddleware
import pandas as pd
import numpy as np

from add_data import session

# Enable caching and specify the cache directory
fastf1.Cache.enable_cache('fastf1_cache')

def get_season_rounds(year: int):
    try:
        events = fastf1.get_event_schedule(year)

        rounds = 1
        try:
            while events.get_event_by_round(rounds) is not None:
                rounds += 1
        except:
            print("Found last event")
        return rounds
    except Exception as e:
        print(f"Error getting season rounds ({year}): {e}")
        return 0  # Return a default value in case of error

def get_results(year: int, round: int, session_type: str):
    try:
        session = fastf1.get_session(year, round, session_type)
        session.load()
        #['DriverNumber', 'BroadcastName', 'Abbreviation', 'DriverId', 'TeamName', 'TeamColor', 'TeamId', 'FirstName', 'LastName', 'FullName', 'HeadshotUrl', 'CountryCode', 'Position', 'ClassifiedPosition', 'GridPosition', 'Q1', 'Q2', 'Q3', 'Time', 'Status', 'Points']
        results_df = session.results[['Abbreviation', 'TeamName', 'Position', 'ClassifiedPosition', 'Points']]

        results = jsonable_encoder(results_df.replace({pd.NaT: None, np.nan: None}).to_dict(orient='records'))

        return results
    except Exception as e:
        print(f"Error getting results ({year}-{round}-{session_type}): {e}")
        return None
#
# # Endpoint to get lap times for a specific driver in a specific session
# @app.get("/laps/{year}/{round}/{session_type}/driver/{driver_number}", description="This endpoint gets the lap times for a given driver in a session.")
# async def get_driver_lap_times(year: int, round: int, session_type: str, driver_number: str):
#     try:
#         session = fastf1.get_session(year, round, session_type)
#         session.load()
#         driver_laps = session.laps.pick_driver(driver_number)
#         # Convert lap times to total seconds for serialization
#         driver_laps['LapTime(s)'] = driver_laps['LapTime'].dt.total_seconds()
#         # ['Time', 'Driver', 'DriverNumber', 'LapTime', 'LapNumber', 'Stint', 'PitOutTime', 'PitInTime', 'Sector1Time', 'Sector2Time', 'Sector3Time', 'Sector1SessionTime', 'Sector2SessionTime', 'Sector3SessionTime', 'SpeedI1', 'SpeedI2', 'SpeedFL', 'SpeedST', 'IsPersonalBest', 'Compound', 'TyreLife', 'FreshTyre', 'Team', 'LapStartTime', 'LapStartDate', 'TrackStatus', 'Position', 'Deleted', 'DeletedReason', 'FastF1Generated', 'IsAccurate']
#         laps_df = driver_laps[['LapNumber', 'LapTime', 'Compound', 'Position', 'Deleted']]
#         laps = jsonable_encoder(laps_df.replace({pd.NaT: None, np.nan: None}).to_dict(orient='records'))
#         return {"driver_lap_times": laps}
#     except Exception as e:
#         raise HTTPException(status_code=404, detail=str(e))
#
# # Endpoint to get telemetry data for a specific driver lap
# @app.get("/telemetry/{year}/{round}/{session_type}/driver/{driver_number}/lap/{lap_number}", description="This endpoint gets the telemetry of a given lap for a driver in a session.")
# async def get_driver_lap_telemetry(year: int, round: int, session_type: str, driver_number: str, lap_number: int):
#     try:
#         session = fastf1.get_session(year, round, session_type)
#         session.load()
#         driver_laps = session.laps.pick_driver(driver_number)
#         lap = driver_laps.loc[driver_laps['LapNumber'] == lap_number]
#         if lap.empty:
#             raise HTTPException(status_code=404, detail="Lap not found for the driver")
#         telemetry = lap.iloc[0].get_car_data().add_distance()
#         # Select relevant telemetry data
#         telemetry_data_df = telemetry[['Time', 'Distance', 'Speed', 'Throttle', 'Brake', 'DRS', 'nGear', 'RPM']]
#         telemetry_data = jsonable_encoder(telemetry_data_df.replace({pd.NaT: None, np.nan: None}).to_dict(orient='records'))
#         # Convert Time deltas to total seconds
#         for point in telemetry_data:
#             if pd.notnull(point['Time']):
#                 # Check if the value has a 'total_seconds' method, implying it's a timedelta or Timestamp
#                 if hasattr(point['Time'], 'total_seconds'):
#                     point['Time'] = point['Time'].total_seconds()
#                 # If it's already a float, do nothing
#                 elif isinstance(point['Time'], (float, int)):
#                     point['Time'] = float(point['Time'])
#                 else:
#                     point['Time'] = None
#             else:
#                 point['Time'] = None
#         return {"telemetry": telemetry_data}
#     except Exception as e:
#         raise HTTPException(status_code=404, detail=str(e))
#

def get_session_drivers(year: int, round: int, session_type: str):
    try:
        session = fastf1.get_session(year, round, session_type)
        session.load()
        drivers = session.drivers
        driver_info = []
        for drv in drivers:
            driver_data = session.get_driver(drv)
            info = {
                "CarNumber": driver_data['DriverNumber'],
                "ShortName": driver_data['Abbreviation'],
                "HeadshotUrl": driver_data['HeadshotUrl'],
                "ConstructorName": driver_data['TeamName'],
            }
            driver_info.append(info)
        return driver_info
    except Exception as e:
        raise HTTPException(status_code=404, detail=str(e))

def get_session_race_control(year: int, round: int):
    try:
        session = fastf1.get_session(year, round, 'R')
        session.load()

        race_control = session.race_control_messages

        # session_race_control = {
        #     "Category", race_control.event['Category'],
        #     "Message", race_control.event['Message'],
        #     "Status", race_control.event['Status'],
        #     "Flag", race_control.event['Flag'],
        #     "Scope", race_control.event['Scope'],
        #     "Sector", race_control.event['Sector'],
        #     "RacingNumber", race_control.event['RacingNumber'],
        #     "Lap", race_control.event['Lap'],
        # }
        return race_control
    except Exception as e:
        raise HTTPException(status_code=404, detail=str(e))


def get_session_info(year: int, round: int, session_type: str):
    try:
        session = fastf1.get_session(year, round, session_type)
        session.load()

        circuit_info = session.get_circuit_info()

        # Extract session information
        session_info = {
            "EventName": session.event['EventName'],
            "Country": session.event['Country'],
            "Location": session.event['Location'],
            "EventFormat": session.event['EventFormat'],
            "Session5": session.event['Session5'],
            "Session5Date": session.event['Session5Date'],
            "Corners": len(circuit_info.corners),
            "Laps": session.total_laps,
            "LapData": session.laps
        }

        return session_info
    except Exception as e:
        return None

# Endpoint to get weather data for a session
def get_weather_data(year: int, round: int, session_type: str):
    try:
        session = fastf1.get_session(year, round, session_type)
        session.load()
        weather_data_df = session.laps.get_weather_data()
        weather_data = jsonable_encoder(weather_data_df.replace({pd.NaT: None, np.nan: None}).to_dict(orient='records'))
        return weather_data
    except Exception as e:
        print(f"Error getting weather data ({year}-{round}): {e}")

# # Endpoint to delete cache for a year
# @app.delete("/cache/{year}", description="This endpoint deletes the stored cache of a given year.")
# async def clear_cache(year: int):
#     """
#     Clears the cached data for the specified year by deleting the corresponding year directory.
#     """
#     try:
#         year_dir = os.path.join(cache_dir, str(year))
#
#         if os.path.exists(year_dir) and os.path.isdir(year_dir):
#             shutil.rmtree(year_dir)
#             return JSONResponse(
#                 content={"message": f"Cache for year {year} cleared successfully."},
#                 status_code=200
#             )
#         else:
#             return JSONResponse(
#                 content={"message": f"No cache directory found for year {year}."},
#                 status_code=200
#             )
#     except Exception as e:
#         raise HTTPException(status_code=500, detail=f"An error occurred while clearing the cache: {e}")