package com.a.eye.gemini.sniffer.cmd;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.a.eye.gemini.sniffer.core.SnifferCoreCard;
import com.a.eye.gemini.sniffer.core.SnifferCoreOfflineReader;
import com.a.eye.gemini.sniffer.core.SnifferCoreOfflineWriter;
import com.a.eye.gemini.sniffer.core.SnifferCoreOnline;

public class GeminiCmd {

	private static Logger logger = LogManager.getFormatterLogger(GeminiCmd.class.getName());

	public static Class<?>[] sniffer = new Class[5];

	public static Integer Cmd_C_Value = 0;

	public static Integer Cmd_I_Value = 0;

	public static String Cmd_P_Value = "";

	public static String Cmd_K_Value = "";

	public static boolean parse(String args[]) throws ParseException {
		Options options = new Options();
		options.addOption(Option
				.builder(GeminiOptions.Cmd_O)
				.longOpt(GeminiOptions.Cmd_Operation)
				.argName(GeminiOptions.Cmd_O_Arg_Name)
				.hasArg()
				.desc(String.format("[ %s  | %s | %s ] 指定需要执行的操作，%s：获取网卡列表，%s：扫描网卡数据立即发送，%s：扫描网卡数据存储到文件，再读取文件发送", GeminiOptions.Cmd_O_Value_Card, GeminiOptions.Cmd_O_Value_Online,
						GeminiOptions.Cmd_O_Value_Offline, GeminiOptions.Cmd_O_Value_Card, GeminiOptions.Cmd_O_Value_Online, GeminiOptions.Cmd_O_Value_Offline)).build());
		options.addOption(Option.builder(GeminiOptions.Cmd_R).longOpt(GeminiOptions.Cmd_Read).desc("当选择离线模式时，读取文件发送分析模块").build());
		options.addOption(Option.builder(GeminiOptions.Cmd_W).longOpt(GeminiOptions.Cmd_Write).desc("当选择离线模式时，抓取网卡数据写入文件").build());
		options.addOption(Option.builder(GeminiOptions.Cmd_I).longOpt(GeminiOptions.Cmd_Interval_Time).argName(GeminiOptions.Cmd_I_Arg_Name).hasArg().desc("设定离线抓取模式下的新文件生成的间隔时间，单位(秒)").build());
		options.addOption(Option.builder(GeminiOptions.Cmd_P).longOpt(GeminiOptions.Cmd_Pcap_File_Path).argName(GeminiOptions.Cmd_P_Arg_Name).hasArg().desc("指定离线抓取模式下文件存储的路径").build());
		options.addOption(Option.builder(GeminiOptions.Cmd_C).longOpt(GeminiOptions.Cmd_Card).argName(GeminiOptions.Cmd_C_Arg_Name).hasArg().desc("指定扫描的网卡编号").build());
		options.addOption(Option.builder(GeminiOptions.Cmd_K).longOpt(GeminiOptions.Cmd_Kafka_Server).argName(GeminiOptions.Cmd_K_Arg_Name).hasArg().desc("指定Kafka的服务器地址").build());
		options.addOption(Option.builder("D").hasArgs().valueSeparator('=').build());

		CommandLineParser parser = new DefaultParser();
		CommandLine line = parser.parse(options, args, false);

		boolean runable = true;
		int i = 0;
		if (!line.hasOption(GeminiOptions.Cmd_O)) {
			runable = false;
			logger.error("未指定需要执行的操作");
		} else if (GeminiOptions.Cmd_O_Value_Card.equals(line.getOptionValue(GeminiOptions.Cmd_O))) {
			sniffer[i] = SnifferCoreCard.class;
			logger.info("获取网卡列表");
		} else if (GeminiOptions.Cmd_O_Value_Online.equals(line.getOptionValue(GeminiOptions.Cmd_O))) {
			if (!line.hasOption(GeminiOptions.Cmd_C)) {
				runable = false;
				logger.error("在线模式，未指定扫描的网卡编号");
			} else if (!line.hasOption(GeminiOptions.Cmd_K)) {
				runable = false;
				logger.error("在线模式，未指定Kafka的服务器地址");
			} else {
				sniffer[i] = SnifferCoreOnline.class;
				Cmd_C_Value = Integer.parseInt(line.getOptionValue(GeminiOptions.Cmd_C));
				Cmd_K_Value = line.getOptionValue(GeminiOptions.Cmd_K);
				logger.info("在线模式，扫描网卡数据立即发送");
			}
		} else if (GeminiOptions.Cmd_O_Value_Offline.equals(line.getOptionValue(GeminiOptions.Cmd_O))) {
			if (!line.hasOption(GeminiOptions.Cmd_R)) {
				runable = false;
				logger.error("离线模式，不开启读取文件发送分析模块");
			} else if (!line.hasOption(GeminiOptions.Cmd_P)) {
				runable = false;
				logger.error("离线模式，开启读取文件发送分析模块，未指定文件读取路径");
			} else if (!line.hasOption(GeminiOptions.Cmd_K)) {
				runable = false;
				logger.error("在线模式，未指定Kafka的服务器地址");
			} else {
				sniffer[i] = SnifferCoreOfflineReader.class;
				i++;

				Cmd_P_Value = line.getOptionValue(GeminiOptions.Cmd_P);
				Cmd_K_Value = line.getOptionValue(GeminiOptions.Cmd_K);
				logger.info("离线模式，读取文件发送分析模块");
			}

			if (!line.hasOption(GeminiOptions.Cmd_W)) {
				runable = false;
				logger.error("离线模式，不开启抓取网卡数据写入文件");
			} else if (!line.hasOption(GeminiOptions.Cmd_P) || !line.hasOption(GeminiOptions.Cmd_I)) {
				runable = false;
				logger.error("离线模式，开启抓取网卡数据写入文件，未指定文件存储路径或者间隔时间");
			} else {
				sniffer[i] = SnifferCoreOfflineWriter.class;
				i++;

				Cmd_I_Value = Integer.parseInt(line.getOptionValue(GeminiOptions.Cmd_I));
				Cmd_P_Value = line.getOptionValue(GeminiOptions.Cmd_P);
				logger.info("离线模式，抓取网卡数据写入文件");
			}
		} else {
			runable = false;
			logger.error("参数错误");
		}

		if (!runable) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar", options);
		}
		return runable;
	}
}