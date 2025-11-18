import dotenv from 'dotenv';
import { exec } from 'child_process';
import fs from 'fs';
import path from 'path'; // Added for cross-platform paths
import { spawn } from "child_process"; // runs java jar
import { v4 as uuidv4 } from 'uuid'; // for unique job ID's

dotenv.config()
const { ROUTE } = process.env;

const VIDEO_DIR = '/videos';
const OUTPUT_DIR = path.join(process.cwd(), 'processor', 'sampleOutput');
const JAR_PATH = path.join(process.cwd(), 'processor', 'target', 'centroid-finder-1.0-SNAPSHOT.jar');

const jobs = {};


export const getVideos = (req,res) => {
    const list = [];
    
    // const files = fs.readdirSync('C:\Users\fredr\OneDrive\SDEV334\centroid-finder\server\videos'); 
    const videoDir = path.join(process.cwd(), 'server', 'videos');
    const files = fs.readdirSync(videoDir);

    files.forEach((file) => {
        list.push(file);
    })
    return res.json({ videos: list });
}
export const getThumbnail = (req,res) => {
    return res.json({ message: 'No Thumbnail' });
}

// Starts new background job to process video with Java JAR
export const processVideo = (req,res) => {
    const { filename } = req.params;
    const inputPath = path.join(VIDEO_DIR, filename);
    const jobId = uuidv4();
    const outputPath = path.join(OUTPUT_DIR, `${jobId}_${filename}`);

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