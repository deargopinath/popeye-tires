package apps.popeyetires.components.content.sidenav;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Value;
import java.util.*;

import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUse;
import com.popeyetires.popeyetires.core.models.SideNav;

public class SideNavWcmUse extends WCMUse {

 private final Logger logger = LoggerFactory.getLogger(SideNavWcmUse.class);
 List<SideNav> navigations = null;
 

 @Override
 public void activate() {
  logger.info("in actiave");
  try {
   Property prop = null;
   SideNav sidenav = null;
   Node currentNode = getResource().adaptTo(Node.class);
   if (currentNode.hasProperty("map")) {
    logger.info("Node has map");
    prop = currentNode.getProperty("map");
   }
   if (prop != null) {
    JSONObject itemJson = null;
    Value[] values = null;
    if (prop.isMultiple()) {
     values = prop.getValues();
    } else {
     values = new Value[1];
     values[0] = prop.getValue();
    }
    navigations = new ArrayList<SideNav>();
     
      for (Value val : values) {
    	  sidenav = new SideNav();
       logger.info("value :" + val);
       itemJson = new JSONObject(val.getString());
       logger.info("itemJson :" + itemJson.getString("name"));
       sidenav.setName(itemJson.getString("name"));
       sidenav.setUrl(itemJson.getString("url"));
       navigations.add(sidenav);
      }
    logger.info("The elements :" + values.length + "size" + navigations.size());

   }
   
   logger.info("End of active");
  } catch (Exception e) {

  }

 }
 
 public List<SideNav> getNavigations() {
  return navigations;
 }
}