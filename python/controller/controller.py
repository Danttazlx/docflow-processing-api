from fastapi import APIRouter, UploadFile, File, HTTPException
from docx import Document
from io import BytesIO

router = APIRouter()


@router.post("/etl/process")
async def process(file: UploadFile = File(...)):
    try:
        content = await file.read()

        Document(BytesIO(content))

        return {
            "fileName": file.filename,
            "status": "success",
            "message": "Document processed successfully"
        }

    except Exception as e:
        raise HTTPException(
            status_code=500,
            detail="Failed to process document"
        ) from e