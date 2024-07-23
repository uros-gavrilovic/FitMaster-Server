package com.mastercode.fitmaster.repository;

import com.mastercode.fitmaster.dto.member.MemberProcedureSearchItem;
import com.mastercode.fitmaster.dto.member.MemberSearchItem;
import com.mastercode.fitmaster.dto.response.SearchResponse;
import com.mastercode.fitmaster.model.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    MemberEntity getByMemberID(Long id);

    Optional<MemberEntity> findByUsername(String username);

    @Procedure(procedureName = "create_member", outputParameterName = "p_member_id")
    Long createProcedure(
        @Param("p_first_name") String firstName,
        @Param("p_last_name") String lastName,
        @Param("p_gender") String gender,
        @Param("p_address") String address,
        @Param("p_phone_number") String phoneNumber,
        @Param("p_birth_date") Date birthDate,
        @Param("p_email") String email,
        @Param("p_username") String username,
        @Param("p_password") String password
    );

    @Procedure(procedureName = "update_member", outputParameterName = "o_member_id")
    Long updateProcedure(
        @Param("p_member_id") Long memberID,
        @Param("p_first_name") String firstName,
        @Param("p_last_name") String lastName,
        @Param("p_gender") String gender,
        @Param("p_address") String address,
        @Param("p_phone_number") String phoneNumber,
        @Param("p_birth_date") Date birthDate,
        @Param("p_email") String email,
        @Param("p_username") String username
    );

    @Procedure(procedureName = "delete_member")
    void deleteProcedure(
        @Param("p_member_id") Long id
    );

    @Query(
        value = "SELECT * FROM search_members(:pageSize, :offset, :fullName, :gender, :status, :sortField, :sortDirection)",
        nativeQuery = true
    )
    List<Object[]> searchProcedure(
        @Param("pageSize") Integer pageSize,
        @Param("offset") Long offset,
        @Param("fullName") String fullName,
        @Param("gender") String gender,
        @Param("status") String status,
        @Param("sortField") String sortField,
        @Param("sortDirection") String sortOrder
    );
}
