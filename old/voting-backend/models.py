from sqlmodel import SQLModel, Field
from typing import Optional
from pydantic import BaseModel
from datetime import date

class User(SQLModel, table=True):
    id: Optional[int] = Field(default=None, primary_key=True)
    username: str

class Vote(SQLModel, table=True):
    id: Optional[int] = Field(default=None, primary_key=True)
    user_id: int = Field(foreign_key="user.id")
    choice: str
    vote_date: date = Field(default_factory=date.today)

class VoteCreate(BaseModel):
    username: str
    choice: str