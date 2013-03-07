package com.naphaso.alice.annotation;

import com.naphaso.alice.XMPPMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/5/13
 * Time: 2:03 AM
 */
public class CommandAnnotationBeanPostProcessor implements DestructionAwareBeanPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(CommandAnnotationBeanPostProcessor.class);

    @Autowired
    private XMPPMessageListener xmppMessageListener;


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, String beanName) throws BeansException {
        ReflectionUtils.doWithMethods(bean.getClass(), new ReflectionUtils.MethodCallback() {
            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                if(Command.class != null && method.isAnnotationPresent(Command.class)
                        && method.equals(ClassUtils.getMostSpecificMethod(method, bean.getClass()))) {
                    Class<?>[] args = method.getParameterTypes();

                    /*if(args.length == 1 && args[0].equals(Message.class)) {
                        register(be);
                    }else */if(args.length == 2 && args[0].equals(Message.class) && args[1].equals(Matcher.class)) {
                        register(bean, method);
                    } else throw new IllegalStateException("@Command on method with invalid args");
                }
            }
        });
        return bean;
    }

    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {

    }


    private void register(final Object bean, final Method method) {
        Command command = method.getAnnotation(Command.class);
        final String patternString = command.pattern();

        xmppMessageListener.registerCommand(new com.naphaso.alice.Command() {
            private WeakReference beanWeakRef = new WeakReference(bean);
            private WeakReference<Method> methodWeakRef = new WeakReference<Method>(method);
            private Pattern pattern = Pattern.compile(patternString);

            @Override
            public boolean process(Message message) {
                Matcher matcher = pattern.matcher(message.getBody());
                if(matcher.matches()) {
                    Object object = beanWeakRef.get();
                    Method method = methodWeakRef.get();
                    try {
                        method.invoke(object, message, matcher);
                    } catch (InvocationTargetException e) {
                        logger.error("annotation call exception", e);
                    } catch (IllegalAccessException e) {
                        logger.error("annotation call exception", e);
                    }
                    return true;
                }
                return false;
            }
        });
    }


}
