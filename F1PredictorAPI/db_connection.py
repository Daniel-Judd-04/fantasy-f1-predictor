from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
import config

def get_engine():
    url = f"mysql+mysqlconnector://{config.DB_USERNAME}:{config.DB_PASSWORD}@{config.DB_HOST}:{config.DB_PORT}/{config.DB_NAME}"
    engine = create_engine(url, echo=False)
    return engine

def get_session():
    engine = get_engine()
    Session = sessionmaker(bind=engine)
    session = Session()
    return session
