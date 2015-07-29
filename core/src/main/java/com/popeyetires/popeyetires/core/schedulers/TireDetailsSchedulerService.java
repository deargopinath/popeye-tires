package com.popeyetires.popeyetires.core.schedulers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.sql.DataSource;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.jcr.api.SlingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.commons.datasource.poolservice.DataSourcePool;
import com.popeyetires.popeyetires.service.TireService;

/**
 * @author supvatti
 *
 */
@Component(label = "TireDetails Scheduled Service", description = "TireDetails scheduled service", immediate = true, metatype = true)
@Properties({
	@Property(label = "TireDetails Scheduled Service", description = "Run this service every day @ 12 get all the tire details.", name = "scheduler.expression", value = "0 26 14 * * ?")
})
@Service
public class TireDetailsSchedulerService implements Runnable {
	private final Logger log = LoggerFactory.getLogger(TireDetailsSchedulerService.class);

	@Reference
	SlingRepository repository;

	@Reference
	private DataSourcePool source;
	
	@Reference
	private TireService service;

	public void run() {

		Connection c = null;
		DataSource dataSource = null;
		try {
			log.info("this is my PopeyeTire Scheduled Service");
			dataSource = (DataSource) source.getDataSource("MysqlDataSource");
			c = dataSource.getConnection();
			log.info("connection ::>>>" + c);
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("Select * from all_tires");
			Session session = this.repository.login(new SimpleCredentials("admin", "admin".toCharArray()));

			Node root = session.getRootNode();
			Node content = root.getNode("content");
			if (!content.hasNode("PopeyeTires")) {
				content.addNode("PopeyeTires", "sling:OrderedFolder");
				log.info("Created PopeyeTires node ");

			}
			Node tires = content.getNode("PopeyeTires");

			if (!tires.hasNode("jcr:content")) {
				tires.addNode("jcr:content", "nt:unstructured");
				log.info("Created jcr node");

			}

			Node jcrContentNode = tires.getNode("jcr:content");
			DecimalFormat df = new DecimalFormat("###.##");
			while (rs.next()) {
				if (!jcrContentNode.hasNode(rs.getString("title").toLowerCase().replaceAll("/", "").replaceAll(" ", "-") + "_" + rs.getString("Full_Size").replaceAll(" ", "-").replaceAll("/", "-"))) {
					log.info(rs.getString("title").toLowerCase().replaceAll("/", "-").replaceAll(" ", "-") + "_" + rs.getString("Full_Size").replaceAll(" ", "-").replaceAll("/", "-"));
					Node material = jcrContentNode.addNode(rs.getString("title").toLowerCase().replaceAll("/", "").replaceAll(" ", "-") + "_" + rs.getString("Full_Size").replaceAll(" ", "-").replaceAll("/", "-"), "nt:unstructured");
					log.info("Created post node :" + material.getName());
					material.setProperty("material", rs.getString("Material"));
					material.setProperty("descriptionEn", rs.getString("Description_EN"));
					material.setProperty("descriptionFr", rs.getString("Description_FR"));
					material.setProperty("title", rs.getString("Title"));
					material.setProperty("fullSize", rs.getString("Full_Size"));
					material.setProperty("treadDepth",rs.getString("Tread_Depth"));
					material.setProperty("snowTire", rs.getString("Snow_Tire"));
					material.setProperty("winterTire",rs.getString("Winter_Tire"));
					material.setProperty("warrantyInKM", rs.getString("Warranty_in_Kilometers"));
					material.setProperty("warrantyInMiles", rs.getString("Warranty_in_Miles"));
					material.setProperty("price", df.format(Math.random() * 1000));
				}
				session.save();
			}
			log.info("Properties has been saved");

			// Code to create pages in content folder
			QueryManager queryManager = session.getWorkspace().getQueryManager();

			Query query = null;
			query = queryManager.createQuery("SELECT * FROM [nt:unstructured] AS s WHERE ISDESCENDANTNODE(s,'" + jcrContentNode.getPath()  + "')", Query.JCR_SQL2);
			QueryResult result = query.execute();

			// logger.info("Node Path ::D> " + node.getPath());
			NodeIterator nodes = result.getNodes();
			// logger.info("NOde size : " +result.getRows().getSize());

			//This code is used to create tire pages
			Node contentPopeyeEn = root.getNode("content/popeyetires/en");
			Node contentPopeyeFr = root.getNode("content/popeyetires/fr");

			while (nodes.hasNext()) {
				Node nextNode = nodes.nextNode();
				log.info("Node Iteration : " + nextNode.getName());
				Node tireEnPageNode = null;
				Node tireFrPageNode = null;
				if (!contentPopeyeEn.hasNode(nextNode.getName())) {
					tireEnPageNode = 	contentPopeyeEn.addNode(nextNode.getName(), "cq:Page");
					log.info("Created cq:page");
				} else {
					tireEnPageNode = contentPopeyeEn.getNode(nextNode.getName());
				}
				
				if (!contentPopeyeFr.hasNode(nextNode.getName())) {
					tireFrPageNode = 	contentPopeyeFr.addNode(nextNode.getName(), "cq:Page");
					log.info("Created cq:page");
				} else {
					tireFrPageNode = contentPopeyeFr.getNode(nextNode.getName());
				}
				
				String tireName = tireEnPageNode.getName();

				Node jcrEnContentNode = null;
				Node jcrFrContentNode = null;
				if (!tireEnPageNode.hasNode("jcr:content")) {
					jcrEnContentNode = tireEnPageNode.addNode("jcr:content", "cq:PageContent");
					jcrFrContentNode = tireFrPageNode.addNode("jcr:content", "cq:PageContent");

					jcrEnContentNode.setProperty("jcr:title", tireName.toUpperCase());
					jcrEnContentNode.setProperty("sling:resourceType", "popeyetires/page/tire-details");
					jcrEnContentNode.setProperty("cq:template", "/apps/popeyetires/templates/tire-details");
					
					jcrFrContentNode.setProperty("jcr:title", tireName.toUpperCase());
					jcrFrContentNode.setProperty("sling:resourceType", "popeyetires/page/tire-details");
					jcrFrContentNode.setProperty("cq:template", "/apps/popeyetires/templates/tire-details");
					log.info("Created jcr node");
				} else {
					jcrEnContentNode = tireEnPageNode.getNode("jcr:content");
					jcrFrContentNode = tireFrPageNode.getNode("jcr:content");
				}

				Node relatedTiresEnNode = jcrEnContentNode.addNode("RelatedTires", "nt:unstructured");
				Node relatedTiresFrNode = jcrFrContentNode.addNode("RelatedTires", "nt:unstructured");
				relatedTiresEnNode.setProperty("sling:resourceType", "popeyetires/components/content/related-tires");
				relatedTiresFrNode.setProperty("sling:resourceType", "popeyetires/components/content/related-tires");
				
				String[] relatedTireInfo = service.getRelatedTireInformation(tireName);
				
				relatedTiresEnNode.setProperty("relatedTires", relatedTireInfo);
				relatedTiresFrNode.setProperty("relatedTires", relatedTireInfo);
				session.save();
			}
		} catch (SQLException e) {
			log.error("SQL exception!!!!!!!!!" + e.getMessage(), e);
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
			log.error("in exception!!!!!!!!!" + e.getMessage(), e);
		} finally{
			try {
				log.info("close the connection");
				c.close();
			} catch (SQLException e) {
				log.error("SQL exception!!!!!!!!!" + e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
