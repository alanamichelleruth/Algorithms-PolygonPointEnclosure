# Algorithms-PolygonPointEnclosure
Project for Algorithms and Data Structures-- number of integer points within a given polygon (by ordered coordinates).

## Problem
Provide a RESTful service which accepts as a POST of JSON as an ordered list of points represented as (x, y) coordinates.  The points define the perimeter of a polygon.
<br />Return the number of points with integer (x, y) coordinates which are fully enclosed by the polygon.  Points on the polygon itself are not included in the count – the points to be included in the count must be wholly within the area bounded by the polygon.  
<br />The grid is at maximum 19x19.  You can assume all points on the polygon are positive integers between 0 and 18 inclusive.
<br />Example input:
<br />{ “inList” : [  { “x” : 2,   “y” : 1 } ,
<br />{ “x” : 2,   “y” : 4 } ,
<br />{ “x” : 11, ”y” : 1 },
<br />{ “x” : 8,   “y” : 4 }  }
<br />Example output:
<br />{ “count” : 13 } 
<br />Erroneous input (e.g. malformed JSON) should be handled gracefully.  

## Deliverable
An HTTP URL was available for the class project yet was destroyed upon completion. Users invoked a RESTful service with a tool such as curl or Postman.
