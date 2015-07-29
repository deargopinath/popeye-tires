package com.popeyetires.popeyetires.service.impl;

import java.util.HashMap;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.jcr.api.SlingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.popeyetires.popeyetires.core.models.TireInfo;
import com.popeyetires.popeyetires.service.TireService;

@Service(TireService.class)
@Component(immediate=true)
public class TireServiceImpl implements TireService {

	private final Logger logger = LoggerFactory.getLogger(TireServiceImpl.class);

	@Reference
	SlingRepository repository;

	Session session;

	@Override
	public TireInfo getTireInformation(String tireName) {

		TireInfo tireInfo = null;
		try {
			session = this.repository.login(new SimpleCredentials("admin", "admin".toCharArray()));

			QueryManager queryManager = session.getWorkspace().getQueryManager();

			Query query = queryManager.createQuery("SELECT * FROM [nt:unstructured] AS s WHERE ISDESCENDANTNODE(s,'/content/PopeyeTires/jcr:content') AND NAME() = '" + tireName + "'", Query.JCR_SQL2);

			QueryResult result = query.execute();

			NodeIterator nodeIterator = result.getNodes();

			while(nodeIterator.hasNext()) {
				tireInfo = new TireInfo();
				Node tireNode = nodeIterator.nextNode();
				PropertyIterator propIter = tireNode.getProperties();
				while(propIter.hasNext()) {
					Property prop = propIter.nextProperty();
					if(prop.getName().equals("title")) {
						tireInfo.setTitle(prop.getString());
					} else if(prop.getName().equals("descriptionEn")) {
						tireInfo.setDescriptionEn(prop.getString());
					} else if(prop.getName().equals("descriptionFr")) {
						tireInfo.setDescriptionFr(prop.getString());
					} else if(prop.getName().equals("treadDepth")) {
						tireInfo.setTreadDepth(prop.getString());
					} else if(prop.getName().equals("warrantyInKM")) {
						tireInfo.setWarrantyInKM(prop.getString());
					} else if(prop.getName().equals("warrantyInMiles")) {
						tireInfo.setWarrantyInMiles(prop.getString());
					} else if(prop.getName().equals("price")) {
						tireInfo.setPrice(prop.getString());
					}else if(prop.getName().equals("snowTire")) {
						tireInfo.setSnowTire(prop.getBoolean());
					}else if(prop.getName().equals("winterTire")) {
						tireInfo.setWinterTire(prop.getBoolean());
					}
				}
			}

			logger.info("Tire Information :" + tireInfo);
			//TireInfo tireInfo = new TireInfo();
			//tireInfo.setTitle("POTENZA RE11 205/R17");
			//tireInfo.setDescription("This is a Potenza tire. All season tire.");
			//tireInfo.setTreadDepth("10.3");
			//tireInfo.setWarranty("18000");
			//tireInfo.setPrice("110.2");

		} catch (LoginException e) {
			e.printStackTrace();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return tireInfo;
	}

	@Override
	public HashMap<String, String> getTireFeaturesInformation(String tireName) {
		HashMap<String, String> tireFeatures = new HashMap<String, String>();
		tireFeatures.put("dryPerformance", "Best");
		tireFeatures.put("wetPerformance", "Better");
		tireFeatures.put("rideComfort", "Best");
		tireFeatures.put("season", "All Season");
		return tireFeatures;
	}

	@Override
	public String[] getRelatedTireInformation(String tireName) {
		//List<TireInfo>  tireInfos = new ArrayList<TireInfo>();
		TireInfo tireInfo = null;
		int count = 0;
		Query query = null;
		QueryResult result2 = null;
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String[] tireInfos = new String[3];
		int i = 0;
		try {
			if(tireName != null) {
				String[] str = tireName.split("\\_"); 
				logger.info("Full Size :" + str[1]);
				session = this.repository.login(new SimpleCredentials("admin", "admin".toCharArray()));

				QueryManager queryManager = session.getWorkspace().getQueryManager();

				query = queryManager.createQuery("SELECT * FROM [nt:unstructured] AS s WHERE ISDESCENDANTNODE(s,'/content/PopeyeTires/jcr:content') "
						+ "AND NAME() NOT LIKE '" + tireName + "' AND s.fullSize= '" + str[1].replace("-", "/") + "' order by s.snowTire DESC, s.winterTire DESC" , Query.JCR_SQL2);
				query.setLimit(3);
				QueryResult result = query.execute();

				count = (int)result.getRows().getSize();

				NodeIterator nodeIterator = result.getNodes();
				//TireInfo tireInfo = null;
				while(nodeIterator.hasNext()) {
					tireInfo = new TireInfo();
					Node tireNode = nodeIterator.nextNode();
					PropertyIterator propIter = tireNode.getProperties();
					while(propIter.hasNext()) {
						Property prop = propIter.nextProperty();
						if(prop.getName().equals("title")) {
							tireInfo.setTitle(prop.getString());
						} else if(prop.getName().equals("descriptionEn")) {
							tireInfo.setDescriptionEn(prop.getString());
						} else if(prop.getName().equals("descriptionFr")) {
							tireInfo.setDescriptionFr(prop.getString());
						} else if(prop.getName().equals("treadDepth")) {
							tireInfo.setTreadDepth(prop.getString());
						} else if(prop.getName().equals("warrantyInKM")) {
							tireInfo.setWarrantyInKM(prop.getString());
						} else if(prop.getName().equals("warrantyInMiles")) {
							tireInfo.setWarrantyInMiles(prop.getString());
						} else if(prop.getName().equals("price")) {
							tireInfo.setPrice(prop.getString());
						} else if(prop.getName().equals("snowTire")) {
							tireInfo.setSnowTire(prop.getString().equals("Y") ? true : false);
						} else if(prop.getName().equals("winterTire")) {
							tireInfo.setWinterTire(prop.getString().equals("Y") ? true : false);
						} else if(prop.getName().equals("fullSize")) {
							tireInfo.setFullSize(prop.getString());
						}
					}
					tireInfo.setTireImage("/content/dam/bst/tires/models/" + tireInfo.getTitle().toLowerCase().replaceAll("/", "").replaceAll(" ", "-") + "/quick-look.png");
					logger.info(gson.toJson(tireInfo));
					tireInfos[i++] = gson.toJson(tireInfo);
				}

				if(count < 3) {
					query = queryManager.createQuery("SELECT * FROM [nt:unstructured] AS s WHERE ISDESCENDANTNODE(s,'/content/PopeyeTires/jcr:content') " + "AND NAME() NOT LIKE '" + tireName + "' order by s.snowTire DESC, s.winterTire DESC" , Query.JCR_SQL2);
					query.setLimit(3-count);
					result2 = query.execute();

					nodeIterator = result2.getNodes();
					//TireInfo tireInfo = null;
					while(nodeIterator.hasNext()) {
						tireInfo = new TireInfo();
						Node tireNode = nodeIterator.nextNode();
						PropertyIterator propIter = tireNode.getProperties();
						while(propIter.hasNext()) {
							Property prop = propIter.nextProperty();
							if(prop.getName().equals("title")) {
								tireInfo.setTitle(prop.getString());
							} else if(prop.getName().equals("descriptionEn")) {
								tireInfo.setDescriptionEn(prop.getString());
							} else if(prop.getName().equals("descriptionFr")) {
								tireInfo.setDescriptionFr(prop.getString());
							} else if(prop.getName().equals("treadDepth")) {
								tireInfo.setTreadDepth(prop.getString());
							} else if(prop.getName().equals("warrantyInKM")) {
								tireInfo.setWarrantyInKM(prop.getString());
							}else if(prop.getName().equals("warrantyInMiles")) {
								tireInfo.setWarrantyInMiles(prop.getString());
							} else if(prop.getName().equals("price")) {
								tireInfo.setPrice(prop.getString());
							}
						}
						tireInfo.setTireImage("/content/dam/bst/tires/models/" + tireInfo.getTitle().toLowerCase().replaceAll("/", "").replaceAll(" ", "-") + "/quick-look.png");
						//tireInfos.add(tireInfo);
						logger.info(gson.toJson(tireInfo));
						tireInfos[i++] = gson.toJson(tireInfo);
					}
				}
			}
			logger.info("Tire Information :" + tireInfo);
		} catch (LoginException e) {
			e.printStackTrace();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return tireInfos;
	}
}
