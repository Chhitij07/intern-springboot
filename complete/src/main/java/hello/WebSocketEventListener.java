package hello;

import java.util.HashSet;
import hello.auth.HttpSecurityConfig;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import hello.auth.DisplayUser;
import hello.auth.WebSocketChannelSecurityConfig;
@Component
public class WebSocketEventListener {

	@Autowired
    private SimpMessagingTemplate template;
	Logger log = Logger.getLogger(WebSocketEventListener.class);
	static double n3[]= {12.9974683333,12.9975283333,12.9974983333,12.9974983333,12.9974983333,12.9974983333,12.9969516667,12.996025,12.99529,12.995215,12.9951383333,12.9951383333,12.995205,12.995205,12.995205,12.995205,12.995205,12.995205,12.995205,12.995205,12.995205,12.9944933333,12.995375,12.9919966667,12.9876183333,12.9808983333,12.982025,12.984235,12.9850266667,12.9862716667,12.9802833333,12.9742333333,12.96368,12.9539833333,12.946915,12.9445166667,12.94107,12.9363783333,12.9352316667,12.931155,12.9262466667,12.9229933333,12.9171566667,12.91293,12.91018,12.907055,12.9013766667,12.89851,12.8985083333,12.8984783333,12.8984783333,12.8984783333,12.8990016667,12.8990016667,12.900885,12.9048916667,12.90644,12.905135,12.8953133333,12.8860466667,12.876105,12.86746,12.861595,12.85674,12.8527083333,12.8497966667,12.8483933333,12.8505916667,12.85341,12.8547733333,12.860675,12.86677,12.8643866667,12.8595166667,12.8591333333,12.857865,12.85704,12.8507983333,12.8416483333,12.836835,12.82114,12.8110583333,12.802435,12.799155,12.7927233333,12.7887783333,12.7862,12.7827316667,12.7782616667,12.7768816667,12.7731533333,12.764775,12.763,12.763,12.763,12.763,12.761745,12.7532116667,12.7466233333,12.7382733333,12.733535,12.7279333333,12.7253533333,12.72014,12.7158466667,12.711955,12.7050966667,12.6974666667,12.688895,12.6850466667,12.6821133333,12.6801433333,12.6767083333,12.6714016667,12.6687966667,12.6657933333,12.66133,12.6540166667,12.6490233333,12.6449483333,12.6397233333,12.6352566667,12.6256716667,12.6162416667,12.6089766667,12.605925,12.60436,12.5988783333,12.5905733333,12.5846966667,12.5790083333,12.5704366667,12.5612383333,12.5523633333,12.5459566667,12.5448283333,12.5448316667,12.54471,12.5447066667,12.5383666667,12.5262566667,12.5156683333,12.5051866667,12.4937616667,12.4842616667,12.472535,12.4608166667,12.449275,12.4388933333,12.4373733333,12.4373733333,12.4373733333,12.4373733333,12.434375,12.4228866667,12.4110466667,12.4018983333,12.3900716667,12.368415,12.3566116667,12.3433283333,12.33088,12.3197166667,12.3077733333,12.2963366667,12.2867383333,12.2746833333,12.2616883333,12.2497166667,12.23833,12.226865,12.2151833333,12.2033866667,12.1915083333,12.1797216667,12.1685716667,12.1604083333,12.1519666667,12.1510933333,12.1510933333,12.1510933333,12.1454333333,12.1336133333,12.1206566667,12.1082583333,12.0975216667,12.0849733333,12.0711616667,12.0572666667,12.04474,12.033315,12.021175,12.0130333333,12.0065716667,12.0065,12.0063533333,12.006305,12.0024366667,11.9942766667,11.9841066667,11.97485,11.9648866667,11.9570366667,11.9453783333,11.9369283333,11.9342,11.9225916667,11.9116516667,11.8997383333,11.88986,11.8815083333,11.8694933333,11.85893,11.84898,11.8388716667,11.8284866667,11.81724,11.8050816667,11.79307,11.7796983333,11.76805,11.7564616667,11.7433133333,11.7341883333,11.7201083333,11.7198583333,11.7159216667,11.710395,11.70503,11.6993083333,11.6919166667,11.6859333333,11.6813666667,11.6810766667,11.6810766667,11.6810766667,11.6810766667,11.6810766667,11.6810766667,11.6764816667,11.676335,11.6773383333,11.6763533333,11.676125,11.6763533333,11.67885,11.6807766667,11.68146,11.6811366667,11.68081,11.6787433333,11.6766466667,11.6754616667,11.6761133333,11.6764,11.6773616667,11.6759966667,11.6766166667,11.6753683333,11.6757066667,11.6716833333,11.6652316667,11.656395,11.6465866667,11.636745,11.6321416667,11.6279066667,11.625955,11.6214133333,11.6097033333,11.5983783333,11.5859083333,11.5742933333,11.562235,11.5497333333,11.53842,11.5272833333,11.5153316667,11.5024183333,11.490145,11.4772466667,11.4653516667,11.4520933333,11.4389933333,11.425655,11.4131466667,11.4017183333,11.388785,11.3760266667,11.3629216667,11.3517016667,11.339475,11.3267366667,11.3145016667,11.3045416667,11.2944933333,11.282055,11.27083,11.2685666667,11.2685666667};
	static double n4[]= {77.5254066667,77.5252283333,77.525375,77.525375,77.525375,77.525375,77.5272783333,77.53081,77.5332116667,77.5331866667,77.5331466667,77.5331466667,77.5332033333,77.5332033333,77.5332033333,77.5332033333,77.5332033333,77.5332033333,77.5332033333,77.5332033333,77.5332033333,77.53536,77.53746,77.5384033333,77.5372566667,77.53684,77.53278,77.528215,77.523755,77.5193933333,77.517,77.5134033333,77.5125566667,77.51997,77.5248283333,77.5276116667,77.5249833333,77.519645,77.5123616667,77.5064866667,77.500505,77.4969566667,77.4904133333,77.485465,77.4818683333,77.47509,77.4681966667,77.46464,77.4646483333,77.464665,77.464665,77.464665,77.46473,77.46473,77.4672533333,77.4708,77.47067,77.4696283333,77.476725,77.487125,77.496265,77.5063716667,77.51866,77.5313633333,77.54484,77.5576816667,77.5702066667,77.5786533333,77.5950933333,77.60857,77.6200933333,77.63169,77.644735,77.6550533333,77.6570633333,77.66027,77.6641516667,77.6685716667,77.6759116667,77.6790616667,77.687085,77.6955083333,77.7045,77.7163333333,77.7265383333,77.7354,77.7481766667,77.760815,77.7736166667,77.7771316667,77.7835116667,77.79148,77.7937833333,77.7937833333,77.7937833333,77.7937833333,77.7952466667,77.8029866667,77.813265,77.8222733333,77.8341066667,77.8457766667,77.8587316667,77.869905,77.88239,77.8937166667,77.9039466667,77.9136716667,77.9226966667,77.9334883333,77.9422116667,77.9546716667,77.9673833333,77.9777666667,77.9900633333,78.0027666667,78.0150966667,78.0263016667,78.0384033333,78.0507483333,78.0622966667,78.072225,78.0800866667,78.08619,78.0959466667,78.1082116667,78.121305,78.1336183333,78.1415033333,78.1510333333,78.1627516667,78.1729566667,78.1816483333,78.1903233333,78.20035,78.2012616667,78.20127,78.2013833333,78.2012616667,78.2032233333,78.2080483333,78.2159833333,78.22338,78.2198,78.217545,78.2146,78.2149083333,78.218425,78.22102,78.2221183333,78.2221183333,78.2221183333,78.2221183333,78.2232583333,78.2265466667,78.2239916667,78.2189216667,78.2191316667,78.2151533333,78.2138016667,78.2155433333,78.2127033333,78.2073966667,78.2085283333,78.2043383333,78.1985516667,78.1966316667,78.1972166667,78.1993983333,78.1951416667,78.19309,78.1902666667,78.1848233333,78.18003,78.172925,78.166415,78.1563316667,78.1485516667,78.1477616667,78.1477616667,78.1477616667,78.1426683333,78.1375483333,78.133285,78.1283366667,78.1192633333,78.112525,78.110165,78.108,78.1054033333,78.1021883333,78.0963616667,78.086165,78.0809733333,78.0809566667,78.0808683333,78.0808266667,78.0781983333,78.069165,78.062045,78.0535483333,78.0501216667,78.0485183333,78.0476483333,78.0544266667,78.0635416667,78.063615,78.0586833333,78.0627766667,78.07229,78.0804516667,78.084025,78.07596,78.06879,78.0666266667,78.0666583333,78.06355,78.0591316667,78.0569583333,78.0579516667,78.0533283333,78.0504483333,78.0506183333,78.057405,78.07347,78.07378,78.0792966667,78.0893633333,78.09799,78.10542,78.1140383333,78.1215983333,78.1280266667,78.1285066667,78.1285066667,78.1285066667,78.1285066667,78.1285066667,78.1285066667,78.13348,78.13789,78.1439783333,78.1499916667,78.1564366667,78.1626716667,78.1686683333,78.172315,78.1735266667,78.1731533333,78.1725266667,78.1686683333,78.1633466667,78.1601,78.156445,78.1492516667,78.145695,78.14344,78.1396816667,78.1344316667,78.1303866667,78.1274083333,78.1235516667,78.1214516667,78.120565,78.12216,78.1281416667,78.1352216667,78.1437016667,78.1464433333,78.145255,78.1469733333,78.14821,78.1435133333,78.14375,78.146085,78.1468266667,78.147575,78.1490716667,78.1499183333,78.1535483333,78.15586,78.158105,78.1590416667,78.15734,78.155965,78.1586433333,78.1598883333,78.1613283333,78.1625083333,78.1646566667,78.16722,78.1702883333,78.17194,78.171965,78.1697583333,78.1664383333,78.1646733333,78.1641116667,78.16425,78.16425};
	static double timeStamp[]= {300.0,
			67.0,
			653.0,
			1.0,
			60.0,
			60.0,
			60.0,
			60.0,
			14.0,
			38.0,
			1.0,
			108.0,
			1.0,
			2.0,
			2911.0,
			45.0,
			9.0,
			203.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			108.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			2.0,
			38.0,
			1.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			42.0,
			78.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			59.0,
			61.0,
			60.0,
			40.0,
			80.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			45.0,
			560.0,
			71.0,
			1.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			43.0,
			58.0,
			60.0,
			60.0,
			61.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			57.0,
			59.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			53.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			59.0,
			61.0,
			60.0,
			59.0,
			61.0,
			26.0,
			29.0,
			441.0,
			1.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			104.0,
			59.0,
			60.0,
			60.0,
			59.0,
			60.0,
			61.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			54.0,
			58.0,
			60.0,
			60.0,
			61.0,
			60.0,
			60.0,
			60.0,
			32.0,
			160.0,
			1.0,
			60.0,
			59.0,
			61.0,
			59.0,
			60.0,
			61.0,
			59.0,
			61.0,
			60.0,
			60.0,
			59.0,
			61.0,
			59.0,
			61.0,
			60.0,
			41.0,
			58.0,
			60.0,
			60.0,
			61.0,
			66.0,
			59.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			68.0,
			59.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			54.0,
			58.0,
			60.0,
			130.0,
			59.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			47.0,
			189.0,
			714.0,
			14.0,
			44.0,
			60.0,
			60.0,
			61.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			59.0,
			60.0,
			55.0,
			58.0,
			60.0,
			60.0,
			61.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			52.0,
			59.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			61.0,
			58.0,
			60.0,
			60.0,
			61.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			53.0,
			58.0,
			60.0,
			60.0,
			61.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			67.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			61.0,
			60.0,
			59.0,
			61.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			53.0,
			59.0,
			60.0,
			18.0,
			59.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			59.0,
			60.0,
			61.0,
			33.0,
			204.0,
			629.0,
			1.0,
			51.0,
			45.0,
			502.0,
			60.0,
			60.0,
			105.0,
			58.0,
			60.0,
			60.0,
			61.0,
			60.0,
			16.0,
			58.0,
			60.0,
			60.0,
			61.0,
			55.0,
			59.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			60.0,
			30.0,
			59.0,
			60.0,
			60.0,
			60.0,
			60.0,
			15.0,
			59.0,
			60.0,
			60.0,
			69.0,
			59.0,
			60.0,
			60.0,
			59.0,
			60.0,
			61.0,
			60.0,
			60.0,
			60.0,
			60.0,
			58.0,
			60.0,
			60.0,
			61.0,
			60.0,
			60.0};
	static int i=23;
	static int interval=1;
	static String name="";
	WebSocketChannelSecurityConfig user=new WebSocketChannelSecurityConfig();
    @Scheduled(fixedDelay=1000)
    public void publishUpdates(){
    	
    	
    	if(interval==timeStamp[i])
    	{
    		if(user.userRegistry()!=null)
    		log.info(user.userRegistry());
    		else
        	{
        		log.info("No users");
        	}
    		interval=1;
    	String text="{\"content\":\""+name+"\",\"Lat\":\""+n3[i]+"\",\"Lng\":\""+n4[i]+"\"}";
        template.convertAndSend("/topic/greetings", text);
        if(i<299)
        {
        	i++;
        }
        else
        {
        	i=23;
        }
    	}
    	else
    	{
    		interval++;
    	}
    }
}