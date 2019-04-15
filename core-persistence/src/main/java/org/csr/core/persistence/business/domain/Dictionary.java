package org.csr.core.persistence.business.domain;

// Generated Oct 29, 2013 10:37:52 AM by Hibernate Tools 3.2.0.b9

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.csr.core.Comment;
import org.csr.core.Persistable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Dictionary generated by hbm2java
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicInsert(true)
@DynamicUpdate(true)
@Table(name = "c_Dictionary")
@Comment(ch = "字典数据",en="pmt_core_Dictionary")
public class Dictionary implements Persistable<Long> {

	/**
	 * (用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Dictionary dictionary;
	private String dictType;
	private String dictValue;
	private Byte hasChild;
	private Integer childCount;
	private Long rank;
	private String remark;
	private List<Dictionary> dictionaries = new ArrayList<Dictionary>(0);

	public Dictionary() {
	}

	public Dictionary(Long id) {
		this.id = id;
	}

	public Dictionary(Long id, Dictionary dictionary, String dictType,
			String dictValue, Byte hasChild, Long rank, String remark,
			List<Dictionary> dictionaries) {
		this.id = id;
		this.dictionary = dictionary;
		this.dictType = dictType;
		this.dictValue = dictValue;
		this.hasChild = hasChild;
		this.rank = rank;
		this.remark = remark;
		this.dictionaries = dictionaries;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parentId")
	public Dictionary getDictionary() {
		return this.dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	@Column(name = "dictType", length = 64)
	public String getDictType() {
		return this.dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	@Column(name = "dictValue", length = 64)
	public String getDictValue() {
		return this.dictValue;
	}

	public void setDictValue(String dictValue) {
		this.dictValue = dictValue;
	}

	@Column(name = "hasChild")
	public Byte getHasChild() {
		return this.hasChild;
	}

	public void setHasChild(Byte hasChild) {
		this.hasChild = hasChild;
	}

	@Column(name = "rank")
	public Long getRank() {
		return this.rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}

	@Column(name = "remark", length = 512)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "dictionary")
	public List<Dictionary> getDictionaries() {
		return this.dictionaries;
	}

	public void setDictionaries(List<Dictionary> dictionaries) {
		this.dictionaries = dictionaries;
	}

	@Transient
	public Integer getChildCount() {
		return childCount;
	}

	public void setChildCount(Integer childCount) {
		this.childCount = childCount;
	}

	@Transient
	public boolean isNew() {
		return null == getId();
	}
}
