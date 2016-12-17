package humming.bee.sprinkler.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import humming.bee.sprinkler.service.model.GroupRunDuration;
import humming.bee.sprinkler.service.model.OverrideTemperatureConfiguration;
import humming.bee.sprinkler.service.model.Sprinkler;
import humming.bee.sprinkler.service.model.SprinklerConfiguration;
import humming.bee.sprinkler.service.model.SprinklerGroup;
import humming.bee.sprinkler.service.model.SprinklerGroupConfiguration;
import humming.bee.sprinkler.service.model.SprinklerGroupRunTime;
import humming.bee.sprinkler.service.model.SprinklerRunTime;

public class SprinklerDb {

	private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/sprinklerdb?autoReconnect=true&useSSL=false&allowMultiQueries=true";
	private static final String USERNAME = "hummingbee";
	private static final String PASSWORD = "humming123";

	private Connection con = null;

	public SprinklerDb() {
		try {
			// getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getConnection() throws Exception {
		// here sprinklerdb is database name, hummingbee is username and
		// humming123 is the password
		if (con == null || con.isClosed()) {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
		}
	}

	/**
	 * Get List of sprinklers and its statuses from Database
	 * 
	 * @return sprinklerList
	 */
	public List<Sprinkler> getSprinkler() {
		List<Sprinkler> sprinklerList = new ArrayList<Sprinkler>();
		Statement stmt = null;
		try {
			getConnection();
			stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select id, name, group_id, status, volume, functional, by_user from sprinkler");
			while (rs.next()) {
				Sprinkler sprinklerObj = new Sprinkler();
				sprinklerObj.setSprinklerId(rs.getInt("id"));
				sprinklerObj.setSprinklerName(rs.getString("name"));
				sprinklerObj.setGroupId(rs.getInt("group_id"));
				sprinklerObj.setSprinklerStatus(rs.getString("status"));
				sprinklerObj.setVolumeOfWater(rs.getDouble("volume"));
				sprinklerObj.setFunctional(rs.getBoolean("functional"));
				sprinklerObj.setByUser(rs.getBoolean("by_user"));
				sprinklerList.add(sprinklerObj);
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				stmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sprinklerList;
	}

	public List<SprinklerGroup> getGroup() {
		List<SprinklerGroup> groupList = new ArrayList<SprinklerGroup>();
		Statement stmt = null;
		try {
			getConnection();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select id,name,status from sprinkler_group");

			while (rs.next()) {
				SprinklerGroup groupObj = new SprinklerGroup();
				groupObj.setGroupId(rs.getInt("id"));
				groupObj.setGroupName(rs.getString("name"));
				groupObj.setStatus(rs.getString("status"));
				groupList.add(groupObj);

			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				stmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return groupList;
	}

	public void setTemperatureConfiguration(OverrideTemperatureConfiguration tempConfiguration) {

		Integer upperLimit = tempConfiguration.getTempUpperLimit();
		Integer lowerLimit = tempConfiguration.getTempLowerLimit();
		int groupId = tempConfiguration.getGroupId();
		PreparedStatement ps = null;

		try {

			getConnection();

			String query = "UPDATE override_temp_config SET upper_limit = coalesce(?, upper_limit), lower_limit =coalesce(?, lower_limit) WHERE group_id = ?; ";

			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setObject(1, upperLimit);// add parameter
			ps.setObject(2, lowerLimit);
			ps.setInt(3, groupId);
			ps.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public List<OverrideTemperatureConfiguration> getTemperatureConfiguration() {

		List<OverrideTemperatureConfiguration> tempConfigurationList = new ArrayList<OverrideTemperatureConfiguration>();
		Statement stmt = null;
		try {
			getConnection();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select id,upper_limit,lower_limit,group_id from override_temp_config");

			while (rs.next()) {
				OverrideTemperatureConfiguration overrideTemperature = new OverrideTemperatureConfiguration();
				overrideTemperature.setId(rs.getInt("id"));
				overrideTemperature.setTempUpperLimit(rs.getInt("upper_limit"));
				overrideTemperature.setTempLowerLimit(rs.getInt("lower_limit"));
				overrideTemperature.setGroupId(rs.getInt("group_id"));
				tempConfigurationList.add(overrideTemperature);
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				stmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tempConfigurationList;
	}

	// ******************************sprinkler**************************************//

	/*
	 * public List<Sprinkler> getAllStatus() { List<Sprinkler> sprinklerList =
	 * new ArrayList<Sprinkler>();
	 * 
	 * try { Class.forName("com.mysql.jdbc.Driver"); Connection con =
	 * DriverManager.getConnection(
	 * "jdbc:mysql://localhost:3306/sprinklerdb?autoReconnect=true&useSSL=false",
	 * "hummingbee", "humming123"); // here sprinklerdb is database name,
	 * hummingbee is username and // humming123 is the password Statement stmt =
	 * con.createStatement(); ResultSet rs = stmt.executeQuery(
	 * "select id,name,group_id,status,volume from sprinkler"); while
	 * (rs.next()) { Sprinkler sprinklerObj = new Sprinkler();
	 * sprinklerObj.setSprinklerId(rs.getInt("id"));
	 * sprinklerObj.setSprinklerName(rs.getString("name"));
	 * sprinklerObj.setGroupId(rs.getInt("group_id"));
	 * sprinklerObj.setSprinklerStatus(rs.getString("status"));
	 * sprinklerObj.setVolumeOfWater(rs.getDouble("volume"));
	 * 
	 * sprinklerList.add(sprinklerObj);
	 * 
	 * }
	 * 
	 * con.close(); } catch (Exception e) { System.out.println(e); } return
	 * sprinklerList; }
	 */

	/**
	 * Get all sprinklers from database by group name
	 * 
	 * @param groupName
	 * @return List
	 */
	public List<Sprinkler> getSprinklersByGroup(String groupName) {
		List<Sprinkler> sprinklerList = new ArrayList<Sprinkler>();
		PreparedStatement ps = null;

		try {
			getConnection();

			// query statement and get result
			String query = "SELECT `sprinkler`.`id`,`sprinkler`.`name`,`sprinkler`.`group_id`,`sprinkler`.`status`,`sprinkler`.`functional`,`sprinkler`.`by_user` "
					+ "FROM `sprinkler` INNER JOIN sprinkler_group ON sprinkler.group_id = sprinkler_group.id "
					+ "where sprinkler_group.name=?;";

			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, groupName);// add parameter

			// get result from db
			ResultSet rs = ps.executeQuery();

			// loop through the result set and add to list
			while (rs.next()) {
				Sprinkler newSprinkler = new Sprinkler();
				newSprinkler.setSprinklerId(rs.getInt("id"));
				newSprinkler.setSprinklerName(rs.getString("name"));
				newSprinkler.setGroupId(rs.getInt("group_id"));
				newSprinkler.setSprinklerStatus(rs.getString("status"));
				newSprinkler.setFunctional((rs.getInt("functional")) == 1 ? true : false);
				newSprinkler.setByUser((rs.getInt("by_user")) == 1 ? true : false);

				sprinklerList.add(newSprinkler);

			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return sprinklerList;
	}

	public void updateSprinklerStatusById(int id, String status) {
		PreparedStatement ps = null;

		try {
			getConnection();

			// query statement and get result
			String query = "UPDATE sprinkler SET status = ? WHERE id = ? ;";

			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, status);// add parameter
			ps.setInt(2, id);// add parameter

			// update db
			ps.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateSprinklerGroupEvent(int month, int dayOfMonth, int groupId, long duration) {
		PreparedStatement ps = null;

		try {
			getConnection();

			// query statement and get result
			String query = "UPDATE sprinkler_group_event SET duration = duration + ? WHERE group_id = ? and day = ? and month = ?";

			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setLong(1, duration);
			ps.setInt(2, groupId);
			ps.setInt(3, dayOfMonth);
			ps.setInt(4, month);

			// update db
			int rows = ps.executeUpdate();
			if (rows == 0) {
				String query1 = "INSERT INTO sprinkler_group_event (group_id, day, month, duration) values(?,?,?,?)";
				ps.close();
				ps = (PreparedStatement) con.prepareStatement(query1);
				ps.setInt(1, groupId);
				ps.setInt(2, dayOfMonth);
				ps.setInt(3, month);
				ps.setLong(4, duration);
				ps.executeUpdate();
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public List<SprinklerGroupConfiguration> getSprinklerGroupConfigurationByDay(String day) {
		List<SprinklerGroupConfiguration> sGroupConfigList = new ArrayList<SprinklerGroupConfiguration>();

		PreparedStatement ps = null;

		try {
			getConnection();
			// query statement and get result
			String query = "select id, group_id, day, start_time, end_time from sprinkler_group_config where day = ?";

			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, day);

			// get result from db
			ResultSet rs = ps.executeQuery();
			// loop through the result set and add to list
			while (rs.next()) {
				SprinklerGroupConfiguration sprinklerGrpConfig = new SprinklerGroupConfiguration();
				sprinklerGrpConfig.setId(rs.getInt("id"));
				sprinklerGrpConfig.setGroupId(rs.getInt("group_id"));
				sprinklerGrpConfig.setDayOfWeek(rs.getString("day"));
				sprinklerGrpConfig.setStartTime(rs.getTime("start_time"));
				sprinklerGrpConfig.setEndTime(rs.getTime("end_time"));

				sGroupConfigList.add(sprinklerGrpConfig);
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return sGroupConfigList;

	}

	/**
	 * Update status of a sprinkler-group (on/off)  as well as sprinkler to database
	 * 
	 * @param groupName
	 * @param status
	 */
	public void updateSprinklerGroupStatusById(int groupId, String status) {
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;

		try {

			getConnection();

			String query1 = "UPDATE sprinkler_group SET status = ? WHERE id = ? ; ";

			ps1 = (PreparedStatement) con.prepareStatement(query1);
			ps1.setString(1, status);// add parameter
			ps1.setInt(2, groupId);
			ps1.executeUpdate();

			String query2 = "UPDATE sprinkler SET status = ? WHERE group_id = ? and functional!=0";

			ps2 = (PreparedStatement) con.prepareStatement(query2);
			ps2.setString(1, status);// add parameter
			ps2.setInt(2, groupId);
			ps2.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps1.close();
				ps2.close();
				con.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Update status of a sprinkler-group (on/off) to database
	 * 
	 * @param groupName
	 * @param status
	 */
	public void updateSprinklerGroupStatusOnly(int groupId, String status) {
		PreparedStatement ps1 = null;
		

		try {

			getConnection();

			String query1 = "UPDATE sprinkler_group SET status = ? WHERE id = ? ; ";

			ps1 = (PreparedStatement) con.prepareStatement(query1);
			ps1.setString(1, status);// add parameter
			ps1.setInt(2, groupId);
			ps1.executeUpdate();
			
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps1.close();
				con.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	
	public List<GroupRunDuration> getGroupRunDuration() {
		List<GroupRunDuration> groupRuns = new ArrayList<GroupRunDuration>();
		Statement stmt = null;
		try {
			getConnection();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select group_id, day, duration durationInSecs "
					+ "from sprinkler_group_event order by month, day");
			while (rs.next()) {
				GroupRunDuration groupRunDuration = new GroupRunDuration();
				groupRunDuration.setDurationInSeconds(rs.getLong("durationInSecs"));
				groupRunDuration.setDay(rs.getInt("day"));
				groupRunDuration.setGroupId(rs.getInt("group_id"));
				groupRuns.add(groupRunDuration);
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				stmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return groupRuns;
	}

	// *******************************************************************************//

	// ******************************configuration************************************//

	/**
	 * Get sprinkler details by name from database
	 * 
	 * @param sprinklerName
	 * @return Sprinkler
	 */
	public Sprinkler getSprinklerByName(String sprinklerName) {
		Sprinkler newSprinkler = new Sprinkler();

		PreparedStatement ps = null;

		try {
			getConnection();

			// query statement and get result
			String query = "SELECT `sprinkler`.`id`,`sprinkler`.`name`,`sprinkler`.`group_id`,`sprinkler`.`status`,`sprinkler`.`functional`,`sprinkler`.`by_user` "
					+ "FROM `sprinkler` where `sprinkler`.`name`=?;";

			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, sprinklerName);// add parameter

			// get result from db
			ResultSet rs = ps.executeQuery();

			// retrieve data to sprinkler object
			if (rs.next()) {
				newSprinkler.setSprinklerId(rs.getInt("id"));
				newSprinkler.setSprinklerName(rs.getString("name"));
				newSprinkler.setGroupId(rs.getInt("group_id"));
				newSprinkler.setSprinklerStatus(rs.getString("status"));
				newSprinkler.setFunctional((rs.getInt("functional")) == 1 ? true : false);
				newSprinkler.setByUser((rs.getInt("by_user")) == 1 ? true : false);
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return newSprinkler;
	}

	/**
	 * Update Sprinkler status(ON/OFF) to database
	 * 
	 * @param sprinklerName
	 * @param status
	 */
	public void updateSprinklerStatus(String sprinklerName, String status) {
		PreparedStatement ps = null;

		try {
			getConnection();

			// query statement and get result
			String query = "UPDATE `sprinkler` SET `status` = ?,`by_user`=0 WHERE `name` = ? ;";

			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, status);// add parameter
			ps.setString(2, sprinklerName);// add parameter

			// update db
			ps.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Update sprinkler to functional/not-functional If changing to
	 * not-functional, also update status=off
	 * 
	 * @param sprinklerName
	 * @param functional
	 */
	public void updateSprinklerFunctional(String sprinklerName, int functional) {
		PreparedStatement ps = null;

		try {
			getConnection();

			// query statement and get result
			String query;
			if (functional == 0)// not-functional
				query = "UPDATE `sprinkler` SET `functional` = ?, status='OFF' WHERE `name` = ? ;";
			else
				query = "UPDATE `sprinkler` SET `functional` = ? WHERE `name` = ? ;";

			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, String.valueOf(functional));// add parameter
			ps.setString(2, sprinklerName);// add parameter

			// update db
			ps.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Update Sprinkler status(ON/OFF) byUser to database
	 * 
	 * @param sprinklerName
	 * @param status
	 */
	public void updateSprinklerStatusByUser(String sprinklerName, String status, boolean byUserOn) {
		PreparedStatement ps = null;

		try {
			getConnection();
			int user = (byUserOn == true) ? 1 : 0;
			// query statement and get result
			String query = "UPDATE `sprinkler` SET `status` = ?,`by_user`=?  WHERE `name` = ? ;";

			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, status);// add parameter
			ps.setString(2, String.valueOf(user));
			ps.setString(3, sprinklerName);// add parameter

			// update db
			ps.executeUpdate();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// *******************************************************************************//

	// ******************************sprinkler
	// group**********************************//

	/**
	 * Gets sprinkler-group by group name from database
	 * 
	 * @param groupName
	 * @return
	 */
	public SprinklerGroup getSprinklerGroupByName(String groupName) {
		SprinklerGroup newGroup = new SprinklerGroup();

		PreparedStatement ps = null;

		try {
			getConnection();

			// query statement and get result
			String query = "SELECT `id`,`name`,`status`  FROM `sprinkler_group` WHERE `name`=?;";

			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, groupName);// add parameter

			// get result from db
			ResultSet rs = ps.executeQuery();

			// retrieve data to sprinkler group object
			if (rs.next()) {
				newGroup.setGroupId(rs.getInt("id"));
				newGroup.setGroupName(rs.getString("name"));
				newGroup.setStatus(rs.getString("status"));

			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return newGroup;
	}

	/**
	 * Update status of a sprinkler-group (on/off) to database
	 * 
	 * @param groupName
	 * @param status
	 */
	public void updateSprinklerGroupStatus(String groupName, String status) {
		PreparedStatement ps = null;

		try {
			getConnection();

			// query statement and get result
			String query = "UPDATE `sprinkler_group` SET `status` = ? WHERE `name` = ? ;";

			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, status);// add parameter
			ps.setString(2, groupName);// add parameter

			// update db
			ps.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// *******************************************************************************//

	// ******************************configuration************************************//

	/**
	 * Get Sprinkler configuration from database by sprinkler name
	 * 
	 * @param sprinklerName
	 * @return
	 */
	public List<SprinklerConfiguration> getConfiguration(String sprinklerName) {
		// sprinklerName="1N";//for testing

		List<SprinklerConfiguration> sprinklerConfigList = new ArrayList<SprinklerConfiguration>();
		PreparedStatement ps = null;

		try {
			getConnection();

			// query statement and get result
			String query = "SELECT `sprinkler_config`.`id`,`sprinkler_config`.`sprinkler_id`,`sprinkler_config`.`day`,"
					+ "`sprinkler_config`.`start_time`,`sprinkler_config`.`end_time` "
					+ "FROM sprinkler_config inner join sprinkler " + "on sprinkler.id=sprinkler_config.sprinkler_id "
					+ "where sprinkler.name=?;";

			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, sprinklerName);// add parameter

			// get result from db
			ResultSet rs = ps.executeQuery();

			// loop through the result set and add to list
			while (rs.next()) {
				SprinklerConfiguration newSprinklerConfig = new SprinklerConfiguration();
				newSprinklerConfig.setId(rs.getInt("id"));
				newSprinklerConfig.setSprinklerId(rs.getInt("sprinkler_id"));
				newSprinklerConfig.setDayOfWeek(rs.getString("day"));
				newSprinklerConfig.setStartTime(rs.getTime("start_time"));
				newSprinklerConfig.setEndTime(rs.getTime("end_time"));

				sprinklerConfigList.add(newSprinklerConfig);

				System.out.print(sprinklerName);
				System.out.println("config id=" + newSprinklerConfig.getId());

			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return sprinklerConfigList;
	}

	// INSERT INTO
	// `sprinkler_config`(`sprinkler_id`,`day`,`start_time`,`end_time`) VALUES (
	// ?,?,?,?) ;

	public int addSprinklerConfiguration(SprinklerConfiguration newSprinklerConfig) {
		PreparedStatement ps = null;
		int i = 0;

		try {
			getConnection();

			// query statement and get result
			String query = "INSERT INTO `sprinkler_config`(`sprinkler_id`,`day`,`start_time`,`end_time`) "
					+ "VALUES ( ?,?,?,?) ;";

			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, String.valueOf(newSprinklerConfig.getSprinklerId()));// add
																					// parameters
			ps.setString(2, newSprinklerConfig.getDayOfWeek());
			ps.setString(3, formatter.format(newSprinklerConfig.getStartTime()));
			ps.setString(4, formatter.format(newSprinklerConfig.getEndTime()));

			// update to db
			i = ps.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return i;

	}

	// SELECT `id`,`sprinkler_id`,`day`,`start_time`,`end_time` FROM
	// `sprinkler_config` WHERE sprinkler_id=1 AND day='Monday'

	public List<SprinklerConfiguration> getSprinklerConfigurationByDay(int sprinklerId, String day) {
		List<SprinklerConfiguration> sConfigList = new ArrayList<SprinklerConfiguration>();

		PreparedStatement ps = null;

		try {
			getConnection();

			// query statement and get result
			String query = "SELECT `id`,`sprinkler_id`,`day`,`start_time`,`end_time` "
					+ "FROM `sprinkler_config` WHERE sprinkler_id=? AND day=?";

			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, String.valueOf(sprinklerId));// add parameters
			ps.setString(2, day);

			// get result from db
			ResultSet rs = ps.executeQuery();
			SprinklerConfiguration newSprinklerConfig = null;
			// loop through the result set and add to list
			while (rs.next()) {
				newSprinklerConfig = new SprinklerConfiguration();
				newSprinklerConfig.setId(rs.getInt("id"));
				newSprinklerConfig.setSprinklerId(rs.getInt("sprinkler_id"));
				newSprinklerConfig.setDayOfWeek(rs.getString("day"));
				newSprinklerConfig.setStartTime(rs.getTime("start_time"));
				newSprinklerConfig.setEndTime(rs.getTime("end_time"));

				sConfigList.add(newSprinklerConfig);
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return sConfigList;

	}

	/**
	 * Delete selected sprinkler config from database
	 * 
	 * @param sConfigId
	 * @return
	 */
	public int deleteSprinklerConfigById(int sConfigId) {
		// DELETE FROM `sprinkler_config` WHERE `id`=?;

		PreparedStatement ps = null;
		int i = 0;

		try {
			getConnection();

			// query statement and get result
			String query = "DELETE FROM `sprinkler_config` WHERE `id`=?;";

			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, String.valueOf(sConfigId));// add parameters

			// delete from db
			i = ps.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return i;
	}

	public boolean isSprinklerConfigExist(SprinklerConfiguration sConfig) {
		int i = 1;
		PreparedStatement ps = null;

		try {
			getConnection();

			// query statement and get result
			String query = "select count(id) from sprinkler_config where `sprinkler_id`=? "
					+ "and `day`=? and ((`start_time` between ? and ?) " + "or (`end_time`  between ? and ?));";

			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, String.valueOf(sConfig.getSprinklerId()));// add
																		// parameters
			ps.setString(2, sConfig.getDayOfWeek());
			ps.setString(3, formatter.format(sConfig.getStartTime()));
			ps.setString(4, formatter.format(sConfig.getEndTime()));
			ps.setString(5, formatter.format(sConfig.getStartTime()));
			ps.setString(6, formatter.format(sConfig.getEndTime()));

			// get result from db
			ResultSet rs = ps.executeQuery();
			// get result
			if (rs.next()) {
				i = rs.getInt("count(id)");
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (i == 0)
			return false;
		else
			return true;

	}

	// *******************************************************************************//

	// ******************************group
	// configuration******************************//

	public int addGroupConfiguration(SprinklerGroupConfiguration newGroupConfig) {
		PreparedStatement ps = null;
		int i = 0;

		try {
			getConnection();

			// query statement and get result
			String query = "INSERT INTO `sprinkler_group_config`(`group_id`,`day`,`start_time`,`end_time`) "
					+ "VALUES ( ?,?,?,?) ;";

			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, String.valueOf(newGroupConfig.getGroupId()));// add
																			// parameters
			ps.setString(2, newGroupConfig.getDayOfWeek());
			ps.setString(3, formatter.format(newGroupConfig.getStartTime()));
			ps.setString(4, formatter.format(newGroupConfig.getEndTime()));

			// update to db
			i = ps.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return i;
	}

	// SELECT `id`,`group_id`,`day`,`start_time`,`end_time` FROM
	// sprinkler_group_config WHERE `group_id`=1 AND `day`='Monday' ;

	public List<SprinklerGroupConfiguration> getGroupConfigurationByDay(int groupId, String day) {
		List<SprinklerGroupConfiguration> gConfigList = new ArrayList<SprinklerGroupConfiguration>();

		PreparedStatement ps = null;

		try {
			getConnection();

			// query statement and get result
			String query = "SELECT `id`,`group_id`,`day`,`start_time`,`end_time` "
					+ "FROM sprinkler_group_config WHERE `group_id`=? AND `day`=? ;";

			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, String.valueOf(groupId));// add parameters
			ps.setString(2, day);

			// get result from db
			ResultSet rs = ps.executeQuery();
			SprinklerGroupConfiguration newGroupConfig = null;
			// loop through the result set and add to list
			while (rs.next()) {
				newGroupConfig = new SprinklerGroupConfiguration();
				newGroupConfig.setId(rs.getInt("id"));
				newGroupConfig.setGroupId(rs.getInt("group_id"));
				newGroupConfig.setDayOfWeek(rs.getString("day"));
				newGroupConfig.setStartTime(rs.getTime("start_time"));
				newGroupConfig.setEndTime(rs.getTime("end_time"));

				gConfigList.add(newGroupConfig);
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return gConfigList;

	}

	/**
	 * Delete selected group config from database
	 * 
	 * @param gConfigId
	 * @return
	 */
	public int deleteGroupConfigById(int gConfigId) {
		// DELETE FROM `sprinkler_config` WHERE `id`=?;

		PreparedStatement ps = null;
		int i = 0;

		try {
			getConnection();

			// query statement and get result
			String query = "DELETE FROM `sprinkler_group_config` WHERE `id`=?;";

			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, String.valueOf(gConfigId));// add parameters

			// delete from db
			i = ps.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return i;
	}

	public boolean isGroupConfigExist(SprinklerGroupConfiguration gConfig) {
		int i = 1;
		PreparedStatement ps = null;

		try {
			getConnection();

			// query statement and get result
			String query = "select count(id) from sprinkler_group_config where `group_id`=? and "
					+ "`day`=? and ((`start_time` between ? and ?) " + "or (`end_time`  between ? and ?));";

			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, String.valueOf(gConfig.getGroupId()));// add
																	// parameters
			ps.setString(2, gConfig.getDayOfWeek());
			ps.setString(3, formatter.format(gConfig.getStartTime()));
			ps.setString(4, formatter.format(gConfig.getEndTime()));
			ps.setString(5, formatter.format(gConfig.getStartTime()));
			ps.setString(6, formatter.format(gConfig.getEndTime()));

			// get result from db
			ResultSet rs = ps.executeQuery();
			// get result
			if (rs.next()) {
				i = rs.getInt("count(id)");
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (i == 0)
			return false;
		else
			return true;

	}

	// *******************************************************************************//

	// ******************************sprinkler run
	// time*******************************//

	/**
	 * Adds start time of sprinkler in run time table
	 * 
	 * @param sRunTime
	 */
	public int addSprinklerRunTime(int sprinklerId) {
		PreparedStatement ps = null;
		int i = 0;

		try {
			getConnection();

			// query statement and get result
			String query = "INSERT INTO `sprinkler_run_time`(`sprinkler_id`,`start_time`) "
					+ "VALUES(?,?);SELECT LAST_INSERT_ID() as runTimeId;";

			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
			Date st = Calendar.getInstance().getTime();

			System.out.println(formatter.format(st.getTime()));

			ps = (PreparedStatement) con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, String.valueOf(sprinklerId));// add parameters
			ps.setString(2, formatter.format(st.getTime()));

			// update to db
			ps.executeUpdate();
			// get primary key value
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				i = rs.getInt(1);
			}

			System.out.println("last insert=" + i);

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return i;
	}

	public void updateSprinklerRunTime(int runTimeId) {
		PreparedStatement ps = null;

		try {
			getConnection();

			// query statement and get result
			String query = "update `sprinkler_run_time` set end_time=? where id=?;";

			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
			Date st = Calendar.getInstance().getTime();

			System.out.println(formatter.format(st.getTime()));

			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, formatter.format(st.getTime()));// add parameters
			ps.setString(2, String.valueOf(runTimeId));

			// update to db
			int i = ps.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public SprinklerRunTime getSprinklerRunTimeToUpdate(int sprinklerId) {
		SprinklerRunTime sRunTime = null;
		PreparedStatement ps = null;

		try {
			getConnection();

			// query statement and get result
			String query = "select `id`,`sprinkler_id`,`start_time`,`end_time` "
					+ "from sprinkler_run_time where `sprinkler_id`=? and end_time is null;";

			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, String.valueOf(sprinklerId));// add parameters

			// get result from db
			ResultSet rs = ps.executeQuery();

			// loop through the result set and add to list
			if (rs.next()) {
				sRunTime = new SprinklerRunTime();
				sRunTime.setId(rs.getInt("id"));
				sRunTime.setSprinklerId(rs.getInt("sprinkler_id"));
				sRunTime.setStartTime(rs.getTime("start_time"));

			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return sRunTime;
	}

	// *******************************************************************************//

	// *************************sprinkler group run
	// time******************************//

	public int addGroupRunTime(int groupId) {
		PreparedStatement ps = null;
		int i = 0;

		try {
			getConnection();

			// query statement and get result
			String query = "INSERT INTO `sprinkler_group_run_time` (`group_id`,`start_time`) VALUES(?,?); SELECT LAST_INSERT_ID() as runTimeId;";

			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
			Date st = Calendar.getInstance().getTime();

			System.out.println(formatter.format(st.getTime()));

			ps = (PreparedStatement) con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, String.valueOf(groupId));// add parameters
			ps.setString(2, formatter.format(st.getTime()));

			// update to db
			ps.executeUpdate();
			// get primary key value
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				i = rs.getInt(1);
			}

			System.out.println("last insert=" + i);

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return i;
	}

	public void updateGroupRunTime(int gRunTimeId) {
		PreparedStatement ps = null;

		try {
			getConnection();

			// query statement and get result
			String query = "update `sprinkler_group_run_time` set end_time=? where id=?;";

			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
			Date st = Calendar.getInstance().getTime();

			System.out.println(formatter.format(st.getTime()));

			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, formatter.format(st.getTime()));// add parameters
			ps.setString(2, String.valueOf(gRunTimeId));

			// update to db
			int i = ps.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public SprinklerGroupRunTime getGroupRunTimeToUpdate(int groupId) {
		SprinklerGroupRunTime gRunTime = null;
		PreparedStatement ps = null;

		try {
			getConnection();

			// query statement and get result
			String query = "select `id`,`group_id`,`start_time`,`end_time` from sprinkler_group_run_time where `group_id`=? and end_time is null;";

			ps = (PreparedStatement) con.prepareStatement(query);
			ps.setString(1, String.valueOf(groupId)); // add parameters

			// get result from db
			ResultSet rs = ps.executeQuery();

			// loop through the result set and add to list
			if (rs.next()) {
				gRunTime = new SprinklerGroupRunTime();
				gRunTime.setId(rs.getInt("id"));
				gRunTime.setGroupId(rs.getInt("group_id"));
				gRunTime.setStartTime(rs.getTime("start_time"));

			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				// close statement and connection
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return gRunTime;
	}

}
