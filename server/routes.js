import { Router } from 'express';
import { getStatus, getThumbnail, getVideos, processVideo } from './controller';

const router = Router();

//GET /api/videos
router.get('/api/videos', getVideos);

//GET /thumbnail/{filename}
router.get('/thumbnail/:filename', getThumbnail);

//POST /process/{filename}
router.get('/process/:filename', processVideo);

//GET /process/{jobId}/status
router.get('/process/:jobId/status', getStatus);