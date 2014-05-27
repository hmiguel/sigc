package models;


public class NER {
	
	public static class Person{
		
		private String person;
		
		public void setPerson(String person) {
			this.person = person;
		}

		public String getPerson() {
			return person;
		}
		
	}
	
	public static class Org{
		
		private String org;
		
		public void setOrganization(String org) {
			this.org = org;
		}

		public String getOrganization() {
			return org;
		}
		
	}

	public static class Date{
		
		private String date;
		
		public void setDate(String date) {
			this.date = date;
		}

		public String getDate() {
			return date;
		}
		
	}
	
	public static class Local{
		
		private String local;
		
		public void setLocal(String local) {
			this.local = local;
		}

		public String getLocal() {
			return local;
		}
		
	}

	public static class Money{
		private String money;
		
		public void setMoney(String money) {
			this.money = money;
		}

		public String getMoney() {
			return money;
		}
	
	}
	
	public static class Percent{
		
		private String percent;
		
		public void setPercent(String percent) {
			this.percent = percent;
		}

		public String getPercent() {
			return percent;
		}
		
	}
	
	public static class Time{
		private String time;
		
		public void setTime(String time) {
			this.time = time;
		}

		public String getTime() {
			return time;
		}
		
		
	}

}
