
    const express = require("express");
    const app = express();
    const cors=require("cors");
    const axios=require("axios");
    const xml2js=require("xml2js");

    const {getLinks}=require("./repo/MiningList");

    //const ejs= require('ejs');
    const port=3000;


    
    app.use(cors())

    app.use(express.json());

    app.set('view-engine', 'ejs');
    app.set('views','views');

    app.listen(port, (req,res) => {
        console.log(`http://localhost:${port}`);

 //minedDataController.startCrawlerRun();
        
        
    })
//----------------------------------------------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------------------------


    const mongoose=require("mongoose");


    mongoose.connect('mongodb://127.0.0.1:27017/newsapp').then(()=>{
        console.log("Open connection");
    })

//     const {defaultInsert, MiningList}=require("./repo/MiningList");

//    ( async () =>{
//     try{
//     await defaultInsert();
//     }catch(error){
//         console.log("avem linkurile default");
//     }})();

//---------------------------------------------------------------------------------------------------------------------------

    
app.get('/', (req, res) => {
    res.render("index.ejs");
})


app.get("/api/feeds",async (req, res) => {

    console.log("am primit cererea");

    list=await getLinks();

    res.json(list);

}  )










