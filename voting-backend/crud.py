from sqlmodel import select, Session
from models import User, Vote
from datetime import date

def get_user_by_username(session: Session, username: str) -> User | None:
    statement = select(User).where(User.username == username)
    result = session.execute(statement)
    user = result.scalar_one_or_none()
    return user


def get_today_votes(session: Session):
    today = date.today()
    statement = (
        select(Vote, User)
        .join(User, User.id == Vote.user_id)
        .where(Vote.vote_date == today)
    )
    results = session.exec(statement).all()
    return [
        {"username": user.username, "choice": vote.choice}
        for vote, user in results
    ]