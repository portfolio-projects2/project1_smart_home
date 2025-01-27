    package afgangsProjekt.automation.systemSecurity;

   // import com.azure.spring.cloud.autoconfigure.aad.AadWebSecurityConfigurerAdapter;
  //  import com.microsoft.azure.spring.autoconfigure.aad.AADAuthenticationFilter;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
    import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
    import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

    // @EnableWebSecurity
  //  @EnableGlobalMethodSecurity(prePostEnabled = true)
    @Configuration("conf")
   @EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
    public class SecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and() // (1)
                    .authorizeRequests().anyRequest().authenticated() // (2)
                    .and()
                   .oauth2ResourceServer().jwt(); // (3)
        }




    }


