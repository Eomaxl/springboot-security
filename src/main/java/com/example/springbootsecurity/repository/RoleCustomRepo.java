package com.example.springbootsecurity.repository;

import com.example.springbootsecurity.model.Role;
import com.example.springbootsecurity.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.websocket.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleCustomRepo {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Role> getRole(User user){
        StringBuilder sql = new StringBuilder().append("SELECT r.name as name\n" +
                "FROM youtube.users u\n" +
                "JOIN youtube.user_role ur ON u.user_id = ur.user_id\n" +
                "JOIN youtube.roles r ON r.role_id = ur.role_id\n" +
                "WHERE u.email='somansin@cisco.com';");
        sql.append("Where 1=1");
        if(user.getEmail() != null){
            sql.append(" and email =: email");
        }
//        NativeQuery<Role> query = ((Session)entityManager.getDelegate()).createNativeQuery(sql.toString());
        NativeQuery<Role> query = (NativeQuery<Role>) entityManager.createNativeQuery(sql.toString());
        if(user.getEmail()!= null){
            query.setParameter("email",user.getEmail());
        }
        query.addScalar("name", StandardBasicTypes.STRING);
        query.setResultListTransformer(Transformers.aliasToBean(Role.class));
        return query.list();
    }
}
