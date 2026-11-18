package com.xiangmu.youxuan.interceptor;

import com.xiangmu.youxuan.context.BaseContext;
import com.xiangmu.youxuan.entity.Employee;
import com.xiangmu.youxuan.entity.User;
import com.xiangmu.youxuan.mapper.EmployeeMapper;
import com.xiangmu.youxuan.mapper.UserMapper;
import com.xiangmu.youxuan.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
public class JwtTokenInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(JwtTokenInterceptor.class);
    private final EmployeeMapper employeeMapper;
    private final UserMapper userMapper;

    public JwtTokenInterceptor(EmployeeMapper employeeMapper,
                               UserMapper userMapper) {
        this.employeeMapper = employeeMapper;
        this.userMapper = userMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler){//传入的请求，
        log.info("拦截器执行：{}",request.getRequestURI());
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()){
            response.setStatus(401);
            return false;
        }
        try {
            Claims claims = JwtUtil.parseEmployeeJWT(token);
            // 1) null 安全取 role（用带类型的 get，避免 .toString() 在 null 上崩）
            String role = claims.get("role", String.class);
            if (role == null) {                       // 缺 role claim → 直接拒
                response.setStatus(401);
                return false;
            }

            // 2) 按角色取对应 id，同样 null 安全
            Long id;
            if ("employee".equals(role)) {
                Object empId = claims.get("employeeId");
                if (empId == null) {
                    response.setStatus(401);
                    return false;
                }   // 缺 employeeId → 拒
                id = Long.valueOf(empId.toString());
            } else if ("user".equals(role)) {
                Object usrId = claims.get("userId");
                if (usrId == null) {
                    response.setStatus(401);
                    return false;
                }   // 缺 userId → 拒
                id = Long.valueOf(usrId.toString());
            } else {
                // 3) 未知 role：显式拒绝，不再“放行且 id 为 null”
                response.setStatus(401);
                return false;
            }

            //校验账号当前是否仍然可用，避免账号被禁用后旧Token继续访问
            if ("employee".equals(role)) {
                Employee employee = employeeMapper.selectById(id);
                if (employee == null
                        || !Objects.equals(employee.getStatus(), 1)) {
                    response.setStatus(403);
                    return false;
                }
            } else {
                User user = userMapper.selectById(id);
                if (user == null
                        || !Objects.equals(user.getStatus(), 1)) {
                    response.setStatus(403);
                    return false;
                }
            }

            // 4) 根据统一路径前缀校验访问角色
            String uri = request.getRequestURI();
            if (uri.startsWith("/employee/")
                    && !"employee".equals(role)) {
                response.setStatus(403);
                return false;
            }

            if (uri.startsWith("/user/")
                    && !"user".equals(role)) {
                response.setStatus(403);
                return false;
            }

            // 5) 只有“已知角色 + 有效账号 + 路径匹配”才写入上下文
            BaseContext.setCurrentId(id);
            BaseContext.setCurrentRole(role);
            return true;

        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }

    }
    @Override
    public void afterCompletion(HttpServletRequest request,HttpServletResponse response,Object handler,Exception exception){
        BaseContext.remove();
    }
}
