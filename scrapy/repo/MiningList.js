const mongoose=require("mongoose");


const MiningListSchema= new mongoose.Schema({

    link:{
        type:String,
        required:true,
        unique:true
    },
    source:{
        type:String,
        required:true,
        unique:false
    },
    tags:{
        titleTag:{
            type:String,
            required:true
        },
        imageTag:{
            type:String,
        required:false
    },
        descriptionTag:{
            type:String,
            required:false
        },
        linkTag:{
            type:String,
            required:true
        },

        itemTag:{
            type:String,
            required:true
        },

        pubDateTag:{
            type:String,
            required:false
        }



        

    }
})



const  MiningList=new mongoose.model("MiningList",MiningListSchema);

const initialList = [
  { link: "https://www.digi24.ro/rss", source: "digi24" },
  { link: "https://www.g4media.ro/feed", source: "g4media" },
  { link: "https://pressone.ro/api/rss", source: "pressone" },
  { link: "https://hotnews.ro/feed", source: "hotnews" },

];







const getLinks= async() =>{
const links=await MiningList.find();
    
    console.log(links);
    
    return links;
}


module.exports= {MiningList,getLinks};