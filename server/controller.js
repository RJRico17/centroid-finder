import dotenv from 'dotenv';
const fs = require('fs');

dotenv.config()
const { ROUTE } = process.env;

export const getVideos = (req,res) => {
    const list = [];
    const files = fs.readdirSync(ROUTE);
    files.forEach((file) => {
        list.push(file);
    })
    console.log(list);
    return list;
}
export const getThumbnail = (req,res) => {
    
}
export const processVideo = (req,res) => {
    
}
export const getStatus = (req,res) => {
    
}