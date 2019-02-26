package tc.View.Server.utils;
public class RuntimeInfoBean {
	private Long availableProcessors;//cpu内核数量
	private Long freeMemory;//jvm空闲内存
	private Long maxMemory;//jvm最大内存占用，同totalMemory
	private String memoryUsage;//内存使用百分比
	private Long totalMemory;//jvm总内存数量
	private String upTime;//运行时间
	private int childCount;//当前客户端数量
	private int childCountLimit;//客户端数量限制
	public Long getAvailableProcessors() {
		return availableProcessors;
	}
	public void setAvailableProcessors(Long availableProcessors) {
		this.availableProcessors = availableProcessors;
	}
	public long getFreeMemory() {
		return freeMemory.longValue();
	}
	public void setFreeMemory(Long freeMemory) {
		this.freeMemory = freeMemory;
	}
	public long getMaxMemory() {
		return maxMemory.longValue();
	}
	public void setMaxMemory(Long maxMemory) {
		this.maxMemory = maxMemory;
	}
	public String getMemoryUsage() {
		return memoryUsage;
	}
	public void setMemoryUsage(String memoryUsage) {
		this.memoryUsage = memoryUsage;
	}
	public long getTotalMemory() {
		return totalMemory.longValue();
	}
	public void setTotalMemory(Long totalMemory) {
		this.totalMemory = totalMemory;
	}
	public String getUpTime() {
		return upTime;
	}
	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}
	public void setChildCount(int childCount){
		this.childCount=childCount;
	}
	public int getChildCount(){
		return this.childCount;
	}
	public void setChildCountLimit(int childCountLimit){
		this.childCountLimit=childCountLimit;
	}
	public int getChildCountLimit(){
		return this.childCountLimit;
	}
	public String toString(){
		return "{'availableProcessors':"+this.availableProcessors+",'freeMemory':"+freeMemory+",'maxMemory':"+maxMemory+",'memoryUsage':'"+memoryUsage+"','totalMemory':"+totalMemory+",'upTime':'"+upTime+"'}";
	}
}
