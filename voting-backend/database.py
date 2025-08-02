from sqlmodel import SQLModel, create_engine, Session

sqlite_file_name = "db.sqlite3"
engine = create_engine(f"sqlite:///{sqlite_file_name}", echo=False)

def init_db():
    SQLModel.metadata.create_all(engine)

def get_session():
    with Session(engine) as session:
        yield session