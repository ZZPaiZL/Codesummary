package com.miniprogram.mapper;

import java.util.List;

import com.mini.program.util.MyMapper;
import com.miniprogram.pojo.MiniSearchRecords;

public interface MiniSearchRecordsMapper extends MyMapper<MiniSearchRecords> {
	public List<String> getHotWords();
}