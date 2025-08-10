from fastapi import APIRouter, HTTPException
from dependencies.database import SessionDep
from models import Vote, VoteCreate, User
from crud import get_user_by_username, get_today_votes
from datetime import date

router = APIRouter()

@router.post("/api/vote")
async def create_vote(vote_create: VoteCreate, session: SessionDep):
    # Resolve user_id from username
    user = get_user_by_username(session, vote_create.username)
    if not user:
        user = User(username=vote_create.username)
        session.add(user)
        session.commit()
        session.refresh(user)

    vote = Vote(
        user_id=user.id,
        choice=vote_create.choice,
        vote_date=date.today()
    )

    session.add(vote)
    session.commit()
    return {"message": "Vote submitted"}

@router.get("/api/vote/today")
def fetch_votes(session: SessionDep):
    return get_today_votes(session)