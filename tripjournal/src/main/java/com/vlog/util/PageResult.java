package com.vlog.util;

import java.util.List;

/**
 * 封装分页信息类
 * @author Administrator
 *
 */
public class PageResult {
		private int page;//当前页数
		private int total;//总页数
		private long counts;//总记录数
		private List<?> rows;//每行显示的内容
		public int getPage() {
			return page;
		}
		public void setPage(int page) {
			this.page = page;
		}
		public int getTotal() {
			return total;
		}
		public void setTotal(int total) {
			this.total = total;
		}
		public long getCounts() {
			return counts;
		}
		public void setCounts(long counts) {
			this.counts = counts;
		}
		public List<?> getRows() {
			return rows;
		}
		public void setRows(List<?> rows) {
			this.rows = rows;
		}
		
}
