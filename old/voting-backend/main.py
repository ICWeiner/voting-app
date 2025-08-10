from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from routes import vote
from database import init_db


app = FastAPI()

# Allow CORS so your frontend on localhost:5173 can call backend on localhost:8000
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:5173"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(vote.router)

@app.on_event("startup")
def on_startup():
    init_db()
