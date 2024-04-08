package bots.telegram.BarBot.config;

import bots.telegram.BarBot.service.BarBotSendMessageService;
import bots.telegram.BarBot.service.BarBotSendMessageServiceImpl;
import bots.telegram.BarBot.service.DataBaseManager;
import bots.telegram.BarBot.service.DataBaseManagerImpl;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.properties")
public class BarBotConfig {

    @Value("${bot.name}")
    String botName;

    @Value("${bot.token}")
    String token;
}
