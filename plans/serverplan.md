# rj notes
Plans and notes for server is that we gotta create 4 apis we can follow similar to the content in josh's class
router => middleware/controller

GET /api/videos
gets all video names

GET /thumbnail/{filename}
thumbnail, could use javacv again and just take at frame one

POST /process/{filename}
start video processing

GET /process/{jobId}/status
gets process of job