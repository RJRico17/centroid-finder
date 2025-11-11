import dotenv from 'dotenv';
import { exec } from 'child_process';
import fs from 'fs';

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
            return;
        }
        if (stdout) {
            console.log(stdout);
            return
        }
        console.log(stderr);
    })
}
export const processVideo = (req,res) => {
    
}
export const getStatus = (req,res) => {
    
}