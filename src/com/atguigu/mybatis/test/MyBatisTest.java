package com.atguigu.mybatis.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.atguigu.mybatis.bean.Department;
import com.atguigu.mybatis.bean.Employee;
import com.atguigu.mybatis.dao.DepartmentMapper;
import com.atguigu.mybatis.dao.EmployeeMapper;
import com.atguigu.mybatis.dao.EmployeeMapperPlus;

/**
 * 1、接口式编程
 * 	原生：		Dao		====>  DaoImpl
 * 	mybatis：	Mapper	====>  xxMapper.xml
 * 
 * 2、SqlSession代表和数据库的一次会话；用完必须关闭；
 * 3、SqlSession和connection一样她都是非线程安全。每次使用都应该去获取新的对象。
 * 4、mapper接口没有实现类，但是mybatis会为这个接口生成一个代理对象。
 * 		（将接口和xml进行绑定）
 * 		EmployeeMapper empMapper =	sqlSession.getMapper(EmployeeMapper.class);
 * 5、两个重要的配置文件：
 * 		mybatis的全局配置文件：包含数据库连接池信息，事务管理器信息等...系统运行环境信息
 * 		sql映射文件：保存了每一个sql语句的映射信息：
 * 					将sql抽取出来。	
 * 
 * 
 * @author lfy
 *
 */
public class MyBatisTest {
	

	public SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		return new SqlSessionFactoryBuilder().build(inputStream);
	}


	public static void main(String[] args)throws  IOException {
		new MyBatisTest().test06();
	}

	
	/**
	 * 测试增删改
	 * 1、mybatis允许增删改直接定义以下类型返回值
	 * 		Integer、Long、Boolean、void
	 * 2、我们需要手动提交数据
	 * 		sqlSessionFactory.openSession();===》手动提交
	 * 		sqlSessionFactory.openSession(true);===》自动提交
	 * @throws IOException 
	 */

	public void test03() throws IOException{
		
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		//1、获取到的SqlSession不会自动提交数据
		SqlSession openSession = sqlSessionFactory.openSession();
		
		try{
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			//测试添加
			Employee employee = new Employee(null, "jerry4",null, "1");
			mapper.addEmp(employee);
			System.out.println(employee.getId());
			
			//测试修改
			//Employee employee = new Employee(1, "Tom", "jerry@atguigu.com", "0");
			//boolean updateEmp = mapper.updateEmp(employee);
			//System.out.println(updateEmp);
			//测试删除
			//mapper.deleteEmpById(2);
			//2、手动提交数据
			openSession.commit();
		}finally{
			openSession.close();
		}
		
	}
	
	

	public void test04() throws IOException{
		
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		//1、获取到的SqlSession不会自动提交数据
		SqlSession openSession = sqlSessionFactory.openSession();
		
		try{
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			/*Employee employee = mapper.getEmpByIdAndLastName(1, "Tom");
			Map<String, Object> map = new HashMap<>();
			map.put("id", 1);
			map.put("lastName", "Tom");
			map.put("tableName", "employee");
			Employee employee = mapper.getEmpByMap(map);*/

			
			List<Employee> like = mapper.getEmpsByLastNameLike("%o%");
			for (Employee employee : like) {
				System.out.println(employee);
			}
			
			/*Map<String, Object> map = mapper.getEmpByIdReturnMap(1);
			System.out.println(map);*/
			Map<String, Employee> map = mapper.getEmpByLastNameLikeReturnMap("%o%");
			System.out.println(map);
			
		}finally{
			openSession.close();
		}
	}
	

	public void test05() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try{
			EmployeeMapperPlus mapper = openSession.getMapper(EmployeeMapperPlus.class);
			Employee empById = mapper.getEmpById(1);
			System.out.println(empById);
			Employee empAndDept = mapper.getEmpAndDept(1);
			System.out.println(empAndDept);
			System.out.println(empAndDept.getDept());
			Employee employee = mapper.getEmpByIdStep(1);
			System.out.println(employee);
			System.out.println(employee.getDept());
		}finally{
			openSession.close();
		}
		
		
	}
	

	public void test06() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		
		try{
			DepartmentMapper mapper = openSession.getMapper(DepartmentMapper.class);
			Department department = mapper.getDeptByIdPlus(1);
			System.out.println(department);
			System.out.println(department.getEmps());
			Department deptByIdStep = mapper.getDeptByIdStep(1);
			System.out.println(deptByIdStep.getDeptName());
			System.out.println(deptByIdStep.getEmps());
		}finally{
			openSession.close();
		}
	}	
	
}
