package cn.liutils.loading;

import java.util.HashMap;
import java.util.Map;

import cn.liutils.core.LIUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public abstract class Loader<T> {
	
	//'default' element (if any): basic search element. 2nd fallback.
	//'parent' element specified in JsonObject: 1st fallback.
	
	Map<String, JsonObject> entries = new HashMap();
	
	Map<String, T> loadedObjects = new HashMap();
	
	static final JsonParser parser = new JsonParser();
	
	public Loader() {}
	
	public void feed(String json) {
		feed(parser.parse(json).getAsJsonObject());
	}
	
	public void feed(JsonObject root) {
		for(Map.Entry<String, JsonElement> entry : root.entrySet()) {
			feedEntry(entry.getKey(), entry.getValue().getAsJsonObject());
		}
	}
	
	public void feedEntry(String name, JsonObject element) {
		entries.put(name, element);
	}
	
	public void loadAll() {
		for(Map.Entry<String, JsonObject> entry : entries.entrySet()) {
			if(entry.getKey() != "default")
				doLoad(entry.getKey(), entry.getValue());
		}
	}
	
	public boolean isLoaded(String name) {
		return loadedObjects.containsKey(name);
	}
	
	public T getObjectLazy(String name) {
		if(!isLoaded(name))
			load(name, entries.get(name));
		return getObject(name);
	}
	
	public T getObject(String name) {
		return loadedObjects.get(name);
	}
	
	private void doLoad(String name, JsonObject object) {
		try {
			T obj = load(name, object);
			if(obj == null) {
				LIUtils.log.error("Didn't load the element " + name + " correctly.");
				return;
			}
			loadedObjects.put(name, obj);
		} catch(Exception e) {
			LIUtils.log.error("An error occured when loading element " + name + ".");
			e.printStackTrace();
		}
	}
	
	public abstract T load(String name, JsonObject object);
	
	/**
	 * @throws NumberFormatException
	 */
	public Double getDouble(String name, Object ...searchRule) {
		JsonPrimitive jp = getProp(name, searchRule);
		return jp == null ? null : jp.getAsDouble();
	}
	
	/**
	 * @throws NumberFormatException
	 */
	public Float getFloat(String name, Object ...searchRule) {
		JsonPrimitive jp = getProp(name, searchRule);
		return jp == null ? null : jp.getAsFloat();
	}
	
	/**
	 * @throws NullPointerException if didn't find the prop
	 * @throws NumberFormatException
	 */
	public int getInt(String name, Object ...searchRule) {
		JsonPrimitive jp = getProp(name, searchRule);
		return jp == null ? null : jp.getAsInt();
	}
	
	public Boolean getBoolean(String name, Object ...searchRule) {
		JsonPrimitive jp = getProp(name, searchRule);
		return jp == null ? null : jp.getAsBoolean();
	}
	
	public String getString(String name, Object ...searchRule) {
		JsonPrimitive jp = getProp(name, searchRule);
		return jp == null ? null : jp.getAsString();
	}
	
	/**
	 * 
	 * @param name element name
	 * @param searchRule <br/>
	 * 	* Do a tree locating on the jsonObject. <br/>
	 *  * type=String: name to lookup, parent=JsonObject <br/>
	 *  * type=int: array index, parent=JsonArray <br/>
	 *  * finally the result must be a JsonPrimitive. otherwise you'll get a null. <br/>
	 *  * Parent fallback: Self->Parent(if any)->Default(if any)
	 * @return null if the object does not exist, or didn't find the primitive.
	 * @throws IllegalArgumentException if given a wrong search rule
	 */
	public JsonPrimitive getProp(String name, Object ...searchRule) {
		JsonObject object = entries.get(name);
		if(object == null)
			return null;
		
		JsonObject defObject = entries.get("default");
		JsonObject parObject = null;
		JsonPrimitive par = (JsonPrimitive) object.get("parent");
		if(par != null && par.isString()) {
			parObject = entries.get(par.getAsString());
		}
		JsonObject[] falls = new JsonObject[] { object, defObject, parObject };
		
		for(JsonObject o : falls) {
			if(o == null)
				continue;
			
			JsonElement current = o;
			
			int searchIndex = 0;
			while(searchIndex < searchRule.length && current != null) {
				Object obj = searchRule[searchIndex];
				if(obj instanceof String) {
					if(!current.isJsonObject())
						break;
					current = current.getAsJsonObject().get((String) obj);
				} else {
					int i = 0;
					try {
						i = (int) obj;
					} catch(Exception e) {
						throw new IllegalArgumentException("Not a string nor an int!");
					}
					
					if(!current.isJsonArray())
						break;
					current = current.getAsJsonArray().get(i);
				}
			}
			
			if(current != null && current.isJsonPrimitive()) {
				return (JsonPrimitive) current;
			}
		}
		
		return null;
	}
	
}
