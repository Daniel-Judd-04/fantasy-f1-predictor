from pydantic import PositiveInt
from sqlalchemy.dialects.mssql import TINYINT, TIMESTAMP
from sqlalchemy.dialects.mysql import SMALLINT
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String, Float, DECIMAL, ForeignKey, Boolean, UniqueConstraint, DateTime
from sqlalchemy.orm import relationship

Base = declarative_base()

class Constructor(Base):
    __tablename__ = 'constructor'
    constructor_id = Column(Integer, primary_key=True, autoincrement=True)
    short_name = Column(String(3), nullable=False, unique=True)
    full_name = Column(String(100), nullable=False)
    country = Column(String(50), nullable=False)
    fantasy_points = Column(Integer, default=0)
    fantasy_price = Column(DECIMAL(4,1), default=0.0)
    active = Column(Boolean, default=True)

    drivers = relationship("Driver", back_populates="constructor")
    results = relationship("Result", back_populates="constructor")

    def __str__(self):
        return f"{self.full_name} ({self.short_name})"

class Driver(Base):
    __tablename__ = 'driver'
    driver_id = Column(Integer, primary_key=True, autoincrement=True)
    constructor_id = Column(Integer, ForeignKey('constructor.constructor_id'), nullable=False)
    short_name = Column(String(3), nullable=False, unique=True)
    full_name = Column(String(100), nullable=False)
    car_number = Column(TINYINT, nullable=False, unique=True)
    country = Column(String(50), nullable=False)
    points = Column(DECIMAL(6,2), default=0.00)
    fantasy_points = Column(Integer, default=0)
    fantasy_price = Column(DECIMAL(4,1), default=0.0)
    active = Column(Boolean, default=True)

    constructor = relationship("Constructor", back_populates="drivers")
    results = relationship("Result", back_populates="driver")

    def __str__(self):
        return f"{self.full_name} ({self.short_name})"

# Add Circuit and GrandPrix classes to models.py later
class Circuit(Base):
    __tablename__ = 'circuit'
    circuit_id = Column(Integer, primary_key=True, autoincrement=True)
    full_name = Column(String(100), nullable=False)
    location_name = Column(String(50), nullable=False)
    country = Column(String(50), nullable=False)
    timezone = Column(String(50), nullable=False)
    length = Column(DECIMAL(6, 3), nullable=False)
    corners = Column(TINYINT, nullable=False)
    laps = Column(TINYINT, nullable=False)
    drs_zones = Column(TINYINT, nullable=False)

    grands_prix = relationship("GrandPrix", back_populates="circuit")

    def __str__(self):
        return f"{self.full_name} ({self.location_name}, {self.country})"

class GrandPrix(Base):
    __tablename__ = 'grand_prix'
    grand_prix_id = Column(Integer, primary_key=True, autoincrement=True)
    circuit_id = Column(Integer, ForeignKey('circuit.circuit_id'), nullable=False)
    full_name = Column(String(100), nullable=False)
    season = Column(SMALLINT, nullable=False)
    round = Column(TINYINT, nullable=False)
    start_time = Column(DateTime, nullable=False)
    sprint = Column(Boolean, nullable=False)
    red_flags = Column(TINYINT, default=0)
    yellow_flags = Column(TINYINT, default=0)
    air_temperature = Column(DECIMAL(5, 2), nullable=False)
    track_temperature = Column(DECIMAL(5, 2), nullable=False)
    humidity = Column(DECIMAL(5, 2), nullable=False)
    wind_speed = Column(DECIMAL(5, 2), nullable=False)
    rainfall = Column(Boolean, nullable=False)

    circuit = relationship("Circuit", back_populates="grands_prix")
    results = relationship("Result", back_populates="grand_prix")

    def __str__(self):
        return f"{self.full_name} ({self.season}-{self.round})"

class Result(Base):
    __tablename__ = 'result'
    driver_id = Column(Integer, ForeignKey('driver.driver_id'), primary_key=True, nullable=False)
    constructor_id = Column(Integer, ForeignKey('constructor.constructor_id'), primary_key=True, nullable=False)
    grand_prix_id = Column(Integer, ForeignKey('grand_prix.grand_prix_id'), primary_key=True, nullable=False)
    session_type = Column(String(1), nullable=False)
    position = Column(TINYINT, nullable=False)
    points = Column(TINYINT, default=0)

    driver = relationship("Driver", back_populates="results")
    constructor = relationship("Constructor", back_populates="results")
    grand_prix = relationship("GrandPrix", back_populates="results")

    def __str__(self):
        return f"Race Result: {self.driver}|{self.constructor} ({self.grand_prix}) [{self.position}]"