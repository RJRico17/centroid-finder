import { Router } from 'express';
import { getResults, getStatus, getThumbnail, getVideos, processVideo } from './controller.js';

const router = Router();

//GET /api/videos
router.get('/api/videos', getVideos);

//GET /thumbnail/{filename}
router.get('/thumbnail/:filename', getThumbnail);

//POST /process/{filename}
router.post('/process/:filename', processVideo);

//GET /process/{jobId}/status
router.get('/process/:jobId/status', getStatus);

//EXTRA GET /api/results
router.get('/api/results', getResults);
export default router;