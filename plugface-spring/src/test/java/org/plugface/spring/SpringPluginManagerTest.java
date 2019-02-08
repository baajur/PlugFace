package org.plugface.spring;

/*-
 * #%L
 * PlugFace :: Spring
 * %%
 * Copyright (C) 2017 - 2018 PlugFace
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.plugface.core.PluginManager;
import org.plugface.core.factory.PluginSources;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SpringPluginManagerTest {
    private AnnotationConfigApplicationContext context;
    private PluginManager sut;

    private void load(Class<?> config) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(config);
        context.refresh();
        this.context = context;
    }

    @Before
    public void setUp() throws Exception {
        load(SpringPluginContextTest.Config.class);
        sut = this.context.getBean(PluginManager.class);
    }

    @After
    public void tearDown() throws Exception {
        context.stop();
    }

    @Test
    public void shouldResolveMixedDependencies() throws Exception {
        sut.loadPlugins(PluginSources.classList(TestPlugin.class));
        final TestPlugin plugin = sut.getPlugin(TestPlugin.class);
        assertNotNull(plugin);
        assertEquals("Plugface: Hello Spring!", plugin.greet());
    }

    @Configuration
    @ImportAutoConfiguration(SpringPluginContextAutoConfiguration.class)
    static class Config {

        @Bean
        public TestBean testBean() {
            return new TestBean();
        }
    }

    public static class TestBean {
        public String greet() {
            return "Hello Spring!";
        }
    }




}
