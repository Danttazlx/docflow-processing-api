from fastapi import APIRouter, UploadFile, File

router = APIRouter()


@router.post("/etl/process")
async def process(file: UploadFile = File(...)):

    try:
        content = file.read()

        document = await Document(BytesIO(content))

        return {
            "fileName": file.filename,
            "status": "success",
            "message": "Document processed successfully"
        }

    except Exception:
        raise HTTPException(
            status_code=500,
            detail="Failed to process document"
        )