from fastapi import FastAPI
from controller.controller import router

app = FastAPI()

app.include_router(router)  