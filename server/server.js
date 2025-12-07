import express from 'express';
import salamanderRouter from './routes.js';

import cors from "cors";
import path from 'path';

const VIDEO_DIR = path.join(process.cwd(), 'videos');
const OUTPUT_DIR = path.join(process.cwd(), 'results')

const app = express();
const PORT = 3000;

app.use(express.static('public'));
app.use(express.urlencoded({extended:true}));
//use to preview videos with
app.use('/videos', express.static(VIDEO_DIR, {
  setHeaders: (res, path) => {
    if (path.endsWith('.mp4')) {
      res.setHeader('Content-Type', 'video/mp4');
      res.setHeader('Content-Disposition', 'inline');
    }
  }
}));
app.use('/results', express.static(OUTPUT_DIR));
app.use(express.json());
app.use(cors());

app.use('/', salamanderRouter);

app.listen(PORT,()=>{
    console.log(`Server is running on http://localhost:${PORT}`);
});