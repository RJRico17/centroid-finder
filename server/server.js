import express from 'express';
import salamanderRouter from './routes.js';

import cors from "cors";

const app = express();
const PORT = 3000;

app.use(express.static('public'));
app.use(express.urlencoded({extended:true}));
app.use(express.json());
app.use(cors());

app.use('/', salamanderRouter);

app.listen(PORT,()=>{
    console.log(`Server is running on http://localhost:${PORT}`);
});