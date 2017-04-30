package pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="curriculum_type1")
public class Classtable {

	@Id
	@SequenceGenerator(name="id_seq",sequenceName="seq_course",initialValue=1,allocationSize=1)
	@GeneratedValue(generator="id_seq",strategy=GenerationType.AUTO)
	private Integer id;
	private String info;
	@Column(columnDefinition="BLOB")
	private String [][] classtable;
	private String parameter;

	public Classtable() {
	}

	public Classtable(String info, String parameter) {
		super();
		this.info = info;
		this.parameter = parameter;
	}

	public Classtable(String info, String[][] classtable, String parameter) {
		this.info = info;
		this.classtable = classtable;
		this.parameter = parameter;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String[][] getClasstable() {
		return classtable;
	}
	public void setClasstable(String[][] classtable) {
		this.classtable = classtable;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
}
