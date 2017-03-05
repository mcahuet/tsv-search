# Tsv-search

## Launch and use application
1. Fill the "query.file.path" field in /conf/application.conf which must be the path of the file to be read.
2. Run the `sbt run` command to start the application.
3. You can use endpoints count and popular :

 - Distinct queries done in 2015: GET http://localhost:900/1/queries/count/2015: returns { count: 573697 }
 - Distinct queries done in Aug: GET http://localhost:900/1/queries/count/2015-08: returns { count: 573697 }
 - Distinct queries done on Aug 3rd: GET http://localhost:900/1/queries/count/2015-08-03: returns { count: 198117 }
 - Distinct queries done on Aug 1st between 00:04:00 and 00:04:59: GET http://localhost:900/1/queries/count/2015-08-01 00:04: returns { count: 617 }
 - Top 3 popular queries done in 2015: GET http://localhost:900/1/queries/popular/2015?size=3: returns
```
{
 "queries": [
  { 
   "query": "http%3A%2F%2Fwww.getsidekick.com%2Fblog%2Fbody-language-advice",
   "count": 6675
  },
  {
   "query": "http%3A%2F%2Fwebboard.yenta4.com%2Ftopic%2F568045",
   "count": 4652 
  },
  {
   "query": "http%3A%2F%2Fwebboard.yenta4.com%2Ftopic%2F379035%3Fsort%3D1",
   "count": 3100
  }]
 }
 ```
 - Top 5 popular queries done on Aug 2nd: GET http://localhost:900/1/queries/popular/2015-08-02?size=5: returns
```
{
 "queries": [
  {
   "query": "http%3A%2F%2Fwww.getsidekick.com%2Fblog%2Fbody-language-advice",
   "count": 2283 
  },
  {
   "query": "http%3A%2F%2Fwebboard.yenta4.com%2Ftopic%2F568045",
   "count": 1943
  },
  {
   "query": "http%3A%2F%2Fwebboard.yenta4.com%2Ftopic%2F379035%3Fsort%3D1",
   "count": 1358
  },
  {
   "query": "http%3A%2F%2Fjamonkey.com%2F50-organizing-ideas-for-every-room-in-your-house%2F",
   "count": 890
  },
  {
   "query": "http%3A%2F%2Fsharingis.cool%2F1000-musicians-played-foo-fighters-learn-to-fly-and-it-was-epic",
   "count": 701
  }]
}
```

### Launch test
1. Run the `sbt test` command to run tests.
