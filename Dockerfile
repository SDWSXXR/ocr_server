FROM jitesoft/tesseract-ocr
LABEL MAINTAINER="xxr<1727905000@qq.com>"
RUN mkdir /usr/local/jdk
WORKDIR /usr/local
ADD /root/reader-server-docker-build/jdk-8u271-linux-x64.tar.gz /usr/local/jdk

COPY ocr-server.jar /usr/local/ocr/ocr-server.jar
COPY chi_sim.traineddata /usr/local/ocr/tessdata/chi_sim.traineddata


#设置系统编码
RUN yum install kde-l10n-Chinese -y
RUN yum install glibc-common -y
RUN localedef -c -f UTF-8 -i zh_CN zh_CN.utf8
ENV LC_ALL zh_CN.UTF-8

ENV JAVA_HOME /usr/local/jdk/jdk1.8.0_271
ENV JRE_HOME /usr/local/jdk/jdk1.8.0_271/jre
ENV PATH $JAVA_HOME/bin:$PATH

CMD java -jar /usr/local/ocr/ocr-server.jar
