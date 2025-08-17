@echo off
echo Setting up MinIO bucket policy for user-images...

REM Apply the bucket policy to allow public read access to user-images
mc policy set-json minio-bucket-policy.json local/user-images

REM Verify the policy was applied
echo.
echo Current bucket policy:
mc policy get local/user-images

echo.
echo MinIO bucket policy setup complete!
echo Images should now be accessible via the proxy endpoint.
