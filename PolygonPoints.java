package csi403;

// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.json.*;
import java.awt.*;
import java.lang.*;

// Extend HttpServlet class
public class PolygonPoints extends HttpServlet {

  public PrintWriter out;

  // Standard servlet method 
  public void init() throws ServletException
  {
      // Do any required initialization here - likely none
  }

  // Standard servlet method - handles a POST operation
  public void doPost(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      response.setContentType("application/json");
      out = response.getWriter();

      try {
          doService(request, response);
      } catch (Exception e){
          e.printStackTrace();
          out.println("{ \"message\" : \"Malformed JSON\"}");
      }
  }

  // Standard servlet method - does not respond to GET
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      // Set response content type and return an error message
      response.setContentType("application/json");
      out = response.getWriter();
      out.println("{ \"message\" : \"Use POST!\"}");
  }


  // Our main worker method
  private void doService(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      // Get received JSON data from HTTP request
      BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
      String jsonStr = "";
      if(br != null){
          jsonStr = br.readLine();
      }

      // Create JsonReader object
      StringReader strReader = new StringReader(jsonStr);
      JsonReader reader = Json.createReader(strReader);
      // Get the singular JSON object (name:value pair) in this message.    
      JsonObject obj = reader.readObject();

      //If more than one key:value pair (not only "inList" present), send an error message
      if(obj.size() > 1){
        out.println("{ \"message\" : \"Invalid number of key:value pairs\" }");
        return;
      }

      // From the object get the array named "inList"
      JsonArray inArray = obj.getJsonArray("inList");

      //Declare variable to hold each element of the inArray
      JsonObject element;
      //Declare Polygon to be empty
      Polygon p = new Polygon();
      //Declare array of Point to contain all vertices
      Point[] vertices = new Point[inArray.size() + 1];
      Point temp; //for each iteration

      //Access and execute the commands from "inList"
      for(int i = 0; i < inArray.size(); i++) {
          element = inArray.getJsonObject(i);

          //If more than one key:value pair (not only "x" and "y" present), send an error message
          if (element.size() > 2) {
              out.println("{ \"message\" : \"Invalid number of key:value pairs\" }");
              return;
          }

          //Add the vertex to the polygon and the array of vertices
          temp = new Point(element.getInt("x"), element.getInt("y"));
          p.addPoint(element.getInt("x"), element.getInt("y"));
          vertices[i] = temp;
      }

      //Set the last index of vertices to be equal to the first
      vertices[vertices.length-1] = vertices[0];

      //Print the number of integer points in the polygon
      out.println("{ \"count\" : " + numContained(p, vertices) + " }");

  }

  // Method to compute the number of integer points contained within the given polygon
    private static int numContained(Polygon p, Point[] vertices){
      //Set count to 0
        int count = 0;

        //Loop through all points in the 19x19 grid (0,0) to (18,18)
        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++){
                //If the polygon contains the point, increment count
                if(p.contains(i, j)){
                    count++;
                    //If the point is on the boundary, decrement count
                    if (onBoundary(i, j, vertices))
                        count--;
                }
            }
        }

        return count;
    }

    // Method to find whether the point is contained on a boundary of the polygon
    private static boolean onBoundary(int x, int y, Point[] vertices){
        //Set result to be false
        boolean result = false;

        //Loop through all the vertices of the polygon
        for(int i = 0; i < vertices.length - 1; i++){
            //If the distance from one vertice to the point + point to next vertice equals distance from the first vertex to the next, point is on the boundary
            if ((distance(vertices[i].x, vertices[i].y, x, y) + distance(x, y, vertices[i+1].x, vertices[i+1].y)) == distance(vertices[i].x, vertices[i].y, vertices[i+1].x, vertices[i+1].y)){
                result = true;
                break;
            }
        }

        return result;
    }


    //method to compute the distance between two points
    private static double distance(int x1, int y1, int x2, int y2){
        return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }

  // Standard Servlet method
  public void destroy() {
      // Do any required tear-down here, likely nothing.
  }
}