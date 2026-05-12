package com.prisma77.crud.repository;

import com.prisma77.crud.domain.Student;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface StudentRepository {

    @Select("SELECT COUNT(*) FROM student WHERE name LIKE CONCAT('%', #{keyword}, '%') OR student_no LIKE CONCAT('%', #{keyword}, '%')")
    long countByKeyword(@Param("keyword") String keyword);

    @Select("SELECT * FROM student WHERE name LIKE CONCAT('%', #{keyword}, '%') OR student_no LIKE CONCAT('%', #{keyword}, '%') ORDER BY id DESC LIMIT #{limit} OFFSET #{offset}")
    List<Student> findByKeywordWithPaging(@Param("keyword") String keyword, @Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT * FROM student WHERE id = #{id}")
    Student findById(@Param("id") Long id);

    @Select("SELECT * FROM student WHERE student_no = #{studentNo}")
    Student findByStudentNo(@Param("studentNo") String studentNo);

    @Insert("INSERT INTO student(student_no, name, email, dept, created_at) VALUES(#{studentNo}, #{name}, #{email}, #{dept}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Student student);

    @Update("UPDATE student SET student_no = #{studentNo}, name = #{name}, email = #{email}, dept = #{dept} WHERE id = #{id}")
    int update(Student student);

    @Delete("DELETE FROM student WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
