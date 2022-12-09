package qsol.qsoljecheonweb.util;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.util.CollectionUtils;
import qsol.qsoljecheonweb.common.model.response.SelectOptionDto;
import qsol.qsoljecheonweb.domain.code.CodeGroup;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class QsolModelMapper {
	private static ModelMapper modelMapper;

	private QsolModelMapper() {

	}

	private synchronized static ModelMapper getInstance() {
		if (modelMapper == null) {
			modelMapper = new ModelMapper();
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

			TypeMap<CodeGroup, SelectOptionDto> typeMap = modelMapper.createTypeMap(CodeGroup.class,
					SelectOptionDto.class);
			typeMap.addMappings(mapper -> {
				mapper.map(src -> src.getCodegp(), SelectOptionDto::setValue);
				mapper.map(src -> src.getCodegpnm(), SelectOptionDto::setLabel);
			});
		}

		return modelMapper;
	}

	public static <S, T> T map(S source, Class<T> destinationType) {
		return getInstance().map(source, destinationType);
	}

	public static <S, T> List<T> map(List<S> source, Class<T> destinationType) {
		List<T> list = new ArrayList<>();
		if (!CollectionUtils.isEmpty(source)) {
			source.forEach(item -> {
				list.add(getInstance().map(item, destinationType));
			});
		}
		return list;
	}

	public static <S, T> void map(S source, T destination) {
		getInstance().map(source, destination);
	}
}
