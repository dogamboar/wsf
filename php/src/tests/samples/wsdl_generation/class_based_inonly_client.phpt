--TEST--
WSDL for class_based inonly - class map provided case
--FILE--
<?php

$url = "http://localhost/services/wsdl_generation/class_based_inonly_service.php?wsdl";
$fp = fopen($url, 'r');
$result = fpassthru($fp);
fclose($fp);

echo $result;

?>
--EXPECT--
<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://schemas.xmlsoap.org/wsdl/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:tns="http://www.wso2.org/php" xmlns:tnx="http://www.wso2.org/php/xsd" xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/" xmlns:http="http://www.w3.org/2003/05/soap/bindings/HTTP/" targetNamespace="http://www.wso2.org/php"><types><xsd:schema elementFormDefault="qualified" targetNamespace="http://www.wso2.org/php/xsd" xmlns:ns0="http://mytest/xsd1"><xsd:element name="echoString"><xsd:complexType><xsd:sequence><xsd:element name="param1" maxOccurs="unbounded" type="ns0:Foo"/><xsd:element name="param2" type="xsd:string"/></xsd:sequence></xsd:complexType></xsd:element><xsd:element name="echoValueResponse"><xsd:complexType><xsd:sequence/></xsd:complexType></xsd:element></xsd:schema><xsd:schema elementFormDefault="qualified" targetNamespace="http://mytest/xsd1" xmlns:ns1="http://mytest/xsd2"><xsd:complexType name="Foo"><xsd:sequence><xsd:element name="foo1" type="xsd:string"/><xsd:element name="foo2" type="xsd:QName"/><xsd:element name="foo3" maxOccurs="unbounded" type="xsd:dateTime"/><xsd:element name="foo4" type="ns1:Bay"/></xsd:sequence></xsd:complexType><xsd:complexType name="Boo"><xsd:sequence><xsd:element name="boo1" maxOccurs="unbounded" type="xsd:string"/><xsd:element name="boo2" maxOccurs="unbounded" type="xsd:duration"/></xsd:sequence></xsd:complexType></xsd:schema><xsd:schema elementFormDefault="qualified" targetNamespace="http://mytest/xsd2" xmlns:ns0="http://mytest/xsd1"><xsd:complexType name="Bay"><xsd:sequence><xsd:element name="bar1" maxOccurs="unbounded" type="xsd:string"/><xsd:element name="bar2" maxOccurs="unbounded" type="ns0:Boo"/></xsd:sequence></xsd:complexType></xsd:schema></types><message name="echoString"><part name="parameters" element="tnx:echoString"/></message><portType name="services_wsdl_generation_class_based_inonly_service.phpPortType"><operation name="echoString"><input message="tns:echoString"/></operation></portType><binding name="services_wsdl_generation_class_based_inonly_service.phpSOAPBinding" type="tns:services_wsdl_generation_class_based_inonly_service.phpPortType"><soap:binding xmlns="http://schemas.xmlsoap.org/wsdl/soap/" transport="http://schemas.xmlsoap.org/soap/http" style="document"/><operation xmlns:default="http://schemas.xmlsoap.org/wsdl/soap/" name="echoString"><soap:operation xmlns="http://schemas.xmlsoap.org/wsdl/soap/" soapAction="http://localhost/services/wsdl_generation/class_based_inonly_service.php/echoValue" style="document"/><input xmlns:default="http://schemas.xmlsoap.org/wsdl/soap/"><soap:body xmlns="http://schemas.xmlsoap.org/wsdl/soap/" use="literal"/></input></operation></binding><service name="services_wsdl_generation_class_based_inonly_service.php"><port xmlns:default="http://schemas.xmlsoap.org/wsdl/soap/" name="services_wsdl_generation_class_based_inonly_service.phpSOAPPort_Http" binding="tns:services_wsdl_generation_class_based_inonly_service.phpSOAPBinding"><soap:address xmlns="http://schemas.xmlsoap.org/wsdl/soap/" location="http://localhost/services/wsdl_generation/class_based_inonly_service.php"/></port></service></definitions>
3298