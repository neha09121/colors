package com.sample.colors;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sample.colors.model.Color;

/**
 * A sample resource that provides access to color list application methods.
 */
@Path(value = "/api/v1.0/colors")
public class ColorList {
	public ColorList() {
		super();
	}

	private static List<Color> list = new ArrayList<Color>();
	private static final String BASE_URL = "http://localhost:8080/colors/jaxrs/api/v1.0/colors";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getColorList() {
		StringBuffer buffer = new StringBuffer();
		if (list != null && list.size() > 0) {
			buffer.append("{");
			for (int i = 0; i < list.size(); ++i) {
				if (i != 0)
					buffer.append(", ");
				buffer.append(list.get(i));
			}
			buffer.append(" }");
		} else {
			buffer.append("The list is empty.");
		}
		return buffer.toString();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getColorById(@PathParam("id") int id) {
		System.out.println("here get ");
		System.out.println(list.toString());
		if ((list != null) && (id > -1) && (id < list.size() - 1)) {
			return list.get(id).toString();
		} else {
			return "Name Not Found";
		}
	}

	@GET
	@Path("/groupbycolor")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response makeColorGroups() {
		System.out.println("here groups ");
		System.out.println(list.toString());
		Collections.sort(list, ColorNameComparator);
		System.out.println("list after sort : " + list.toString());
		int id = 1;
		JSONArray responseArray = new JSONArray();
		JSONObject json = null;
		JSONObject responseJson = new JSONObject();
		try {
			for (int i = 0; i < list.size(); ++i) {
				list.get(i).setId(id);
				++id;
				json = new JSONObject();
				json.put("colorname", list.get(i).getColor());

				json.put("uri", BASE_URL + "/" + list.get(i).getId());
				
				responseArray.put(json);
			}
		
		responseJson.put("colors", responseArray);
		System.out.println("responseJson : "+responseJson);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(500).entity(responseJson.toString()).build();
		}
		System.out.println("list after adjusting ids : " + list.toString());
		return Response.status(200).entity(responseJson.toString()).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveColor(InputStream incomingData) {
		System.out.println("here");
		StringBuilder listBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				listBuilder.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error parsing json input stream.");
		}
		String colorname = null;
		JSONObject responseJson = new JSONObject();
		try {
			JSONObject json = new JSONObject(listBuilder.toString());
			colorname = json.getString("colorname");
			if (list == null) {
				list = new ArrayList<Color>();
			}
			Color item = new Color(colorname);
			System.out.println("item : " + item);
			list.add(item);
			System.out.println("list : " + list.toString());

			// create return object with uri attribute
			JSONObject responseJsonAttr = new JSONObject();
			responseJsonAttr.put("colorname", colorname);
			responseJsonAttr.put("uri", BASE_URL + "/" + item.getId());
			responseJson.put("color", responseJsonAttr);

		} catch (JSONException e) {
			e.printStackTrace();
			return Response.status(500).entity(listBuilder.toString()).build();
		}

		// return HTTP response 200 in case of success
		return Response.status(200).entity(responseJson.toString()).build();
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateColor(@PathParam("id") int id,
			InputStream incomingData) {
		System.out.println("here put ");
		StringBuilder listBuilder = new StringBuilder();
		System.out.println(list.toString());
		JSONObject responseJson = new JSONObject();
		if ((list != null) && (id - 1 > -1) && (id - 1 <= list.size() - 1)) {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						incomingData));
				String line = null;
				while ((line = in.readLine()) != null) {
					listBuilder.append(line);
				}
			} catch (Exception e) {
				System.out.println("Error parsing json input stream.");
			}
			String colorname = null;

			JSONObject json;
			try {
				json = new JSONObject(listBuilder.toString());
				JSONObject newjson = new JSONObject();
				colorname = (String) json.get("colorname");
				newjson.put("colorname", colorname);
				newjson.put("uri", BASE_URL + "/" + id);
				responseJson.put("color", newjson);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			Color item = new Color(id, colorname);
			System.out.println("item : " + item);
			list.remove(id - 1);
			list.add(id - 1, item);
			System.out.println("list : " + list.toString());

			return Response.status(200).entity(responseJson.toString()).build();

		} else {
			return Response.status(500).entity(responseJson.toString()).build();
		}
	}

	public static Comparator<Color> ColorNameComparator = new Comparator<Color>() {

		public int compare(Color color1, Color color2) {

			String colorName1 = color1.getColor().toUpperCase();
			String colorName2 = color2.getColor().toUpperCase();

			// ascending order
			return colorName1.compareTo(colorName2);

		}

	};
}
