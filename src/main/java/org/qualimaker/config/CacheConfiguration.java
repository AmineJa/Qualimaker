package org.qualimaker.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(org.qualimaker.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.PersistentToken.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.User.class.getName() + ".persistentTokens", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Formation.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Formation.class.getName() + ".employes", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Abscence.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Conge.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Critereevaluation.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DemandeFormation.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.DomaineCompetence.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Employe.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Employe.class.getName() + ".conges", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Employe.class.getName() + ".formations", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Employe.class.getName() + ".fonctions", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Etat.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Evaluation.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.EvaluationFormation.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Fichierjoint.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Fonction.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Fonction.class.getName() + ".employees", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Formateur.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Formateur.class.getName() + ".typeformateurs", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Carriere.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.FormationComp.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Groupe.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Integre.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Jour.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.NatureAbs.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Natureformation.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Programme.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Serviice.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.Sites.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.TypeConge.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.TypeContrat.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.TypeContrat.class.getName() + ".carrieres", jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.TypeFormateur.class.getName(), jcacheConfiguration);
            cm.createCache(org.qualimaker.domain.TypeFormation.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
