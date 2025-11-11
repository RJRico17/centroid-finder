import dotenv from 'dotenv';
import { exec } from 'child_process';
import fs from 'fs';
import { stderr, stdout } from 'process';
import crypto from 'crypto';
import { URLSearchParams } from 'url';

dotenv.config()
const { ROUTE } = process.env;

export const getVideos = (req,res) => {
    const list = [];
    const files = fs.readdirSync('videos');
    files.forEach((file) => {
        list.push(file);
    })
    console.log(list);
    return list;
}
export const getThumbnail = (req,res) => {
    const file = req.params.filename;
    exec(`ffmpeg -i videos/${file} -frames:v 1 thumbnail.jpeg`, (err, stdout, stderr) => {
        if (err) {
            console.log(err);
            return {
                "error": err
            }
        }
        if (stdout) {
            console.log(stdout);
            return
        }
        console.log(stderr);
    })
}

export const processVideo = async (req,res) => {
    // const query = window.location.search;
    const file = req.params.filename;
    // const targetColor = req.params.targetColor;
    // const threshold = req.params.threshold;
    const targetColor = req.query.targetColor;
    const threshold = req.query.threshold;
    await exec(`java -jar ../processor/target/centroid-finder-1.0-SNAPSHOT.jar ${file} output.csv ${targetColor} ${threshold}`, (err, stdout, stderr) => {
        if (err) {
            console.log(err);
            return {
                "error": err
            }
        }
        if (stdout) {
            console.log(stdout);
            const uuid = crypto.randomUUID();
            return {
                "jobId": uuid
            }
        }
        console.log(stderr);
    })
}
export const getStatus = (req,res) => {
    
}