package com.prisma77.crud.repository;

import com.prisma77.crud.domain.Course;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CourseRepository {

    @Select("SELECT COUNT(*) FROM course WHERE title LIKE CONCAT('%', #{keyword}, '%') OR code LIKE CONCAT('%', #{keyword}, '%')")
    long countByKeyword(@Param("keyword") String keyword);

    @Select("SELECT * FROM course WHERE title LIKE CONCAT('%', #{keyword}, '%') OR code LIKE CONCAT('%', #{keyword}, '%') ORDER BY id DESC LIMIT #{limit} OFFSET #{offset}")
    List<Course> findByKeywordWithPaging(@Param("keyword") String keyword, @Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT * FROM course WHERE id = #{id}")
    Course findById(@Param("id") Long id);

    @Select("SELECT * FROM course ORDER BY id DESC")
    List<Course> findAll();

    @Select("SELECT * FROM course WHERE code = #{code}")
    Course findByCode(@Param("code") String code);

    @Insert("INSERT INTO course(code, title, professor, credit, created_at) VALUES(#{code}, #{title}, #{professor}, #{credit}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Course course);

    @Update("UPDATE course SET code = #{code}, title = #{title}, professor = #{professor}, credit = #{credit} WHERE id = #{id}")
    int update(Course course);

    @Delete("DELETE FROM course WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}