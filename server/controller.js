import dotenv from 'dotenv';
import { exec } from 'child_process';
import fs from 'fs';
import path from 'path'; // Added for cross-platform paths
import { spawn } from "child_process"; // runs java jar
import { v4 as uuidv4 } from 'uuid'; // for unique job ID's

dotenv.config()
const { ROUTE } = process.env;

// const VIDEO_DIR = path.join(process.cwd(), 'server', 'videos');
const VIDEO_DIR = '/videos'
const OUTPUT_DIR = '/results';

const JAR_PATH = path.join(process.cwd(), 'processor', 'target', 'centroid-finder-1.0-SNAPSHOT.jar');

const jobs = {};


export const getVideos = (req,res) => {
    const list = [];
    
    // const files = fs.readdirSync('C:\Users\fredr\OneDrive\SDEV334\centroid-finder\server\videos'); 
    const files = fs.readdirSync(VIDEO_DIR);


    files.forEach((file) => {
        list.push(file);
    })
    return res.json({ videos: list });
}
export const getThumbnail = (req,res) => {
    const file = req.params.filename;
    const fullpath = path.join(VIDEO_DIR, file);
    res.setHeader("Content-Type", "image/jpeg");
    exec(`ffmpeg -i "${fullpath}" -ss 00:00:01 -frames:v 1 -f image2 pipe:1`, 
    { encoding: "buffer", maxBuffer: 10 * 1024 * 1024 }, (err, stdout, stderr) => {
        if (err) {
            console.log(err);
            return;
        }
        console.log(stderr);
        res.send(stdout);
    })
}

// Starts new background job to process video with Java JAR
export const processVideo = (req,res) => {
    const { filename } = req.params;
    const inputPath = path.join(VIDEO_DIR, filename);

    // validate the file exists before processing
    if (!fs.existsSync(inputPath)) {
        return res.status(400).json({
            error: `File not found: ${filename}`
        });
    }
    // Validate if the file extension is supported
    if (!filename.toLowerCase().endsWith(".mp4")) {
        return res.status(400).json({
            error: "Invalid file type. Only MP4 videos are supported."
        });
    }

    const jobId = uuidv4();
    const outputPath = path.join(OUTPUT_DIR, `${jobId}_${filename}`);
    console.log(`Job started @ ${jobId}`)

    // Launch the JAR as a detached process
    const child = spawn('java', ['-jar', JAR_PATH, inputPath, outputPath], {
        detached: true,
        stdio: 'ignore'
    });

    // Save job metadata
    jobs[jobId] = { status: 'running', outputPath };

    // Allow process to continue independently
    child.unref();

    // Immediately return the new job info
    res.json({ jobId, status: 'running' });
}

// returns current status of a job
export const getStatus = (req,res) => {

    const { jobId } = req.params;
    const job = jobs[jobId];

    if (!job) {
        // Express will send this 404 response directly
        return res.status(404).json({ error: 'Job not found' });
    }

    // Mark as done if the output file now exists
    if (fs.existsSync(job.outputPath)) {
        job.status = 'done';
    }

    res.json({
        jobId,
        status: job.status,
        output: job.status === 'done' ? job.outputPath : null
    });
}