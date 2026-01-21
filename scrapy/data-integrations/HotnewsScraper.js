const puppeteer = require("puppeteer");

async function ScrapeHotNews() {

const browser=await puppeteer.launch(); 
const page= await browser.newPage(); 

await page.goto("https://hotnews.ro/c/actualitate");

let data= await page.evaluate(() => {
  return Array.from(document.querySelectorAll("a")).map(a=>({
    href:a.href,
  }));
})
console.log(data.length);
const regex = /^https:\/\/hotnews\.ro\/([a-zA-Z0-9]+-)*[a-zA-Z0-9]+$/;

const filterData=data.filter(a=>a.text=' ').filter(a=>regex.test(a.href)).slice(1,80);



const results=[];
const set= new Set();

const mappedData=await Promise.all(filterData.map(async(obj) => {

  const articlePage=await browser.newPage();
  
    await articlePage.goto(obj.href,{timeout:120000});
  
    const newObj=await articlePage.evaluate(()=>{
  
      const newObj={}

      h1=document.querySelector("h1");
      description=document.querySelector("p");
      imageSection=document.querySelector(".post-thumbnail");
      image=imageSection.querySelector("img");

      newObj.title=h1.innerText;
      newObj.description=description.innerText;
      newObj.image_url=image.src;
      newObj.source_url="https://hotnews.ro"    
      return newObj;
    })
    newObj.language="ro";
    newObj.category='politica-interna';
    newObj.article_url=obj.href;

    console.log("Se incarca articol Hotnews")

    return newObj;
}))


return mappedData.filter(obj=>{
  if(set.has(obj.article_url)){
    return false;
  }
  set.add(obj.article_url);
  return true;
});
}

module.exports = ScrapeHotNews;

