import * as readline from 'readline';

type Attribute = {
  name: string;
  type: string;
  isNullable: boolean;
};

type Relation = {
  relatedEntity: string;
  relationType: string;
};

type Entity = {
  name: string;
  attributes: Attribute[];
  relations: Relation[];
};

const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout,
});

const askQuestion = (question: string): Promise<string> => {
  return new Promise(resolve => {
    rl.question(question, answer => resolve(answer.trim()));
  });
};

const createEntity = async (): Promise<Entity> => {
  const name = await askQuestion('Nombre de la entidad: ');
  const attributes: Attribute[] = [];
  const relations: Relation[] = [];

  let addMoreAttributes = 'sí';
  while (addMoreAttributes.toLowerCase() === 'sí') {
    const attrName = await askQuestion('Nombre del atributo: ');

    attributes.push({
      name: attrName,
      type: 'string',
      isNullable: true,
    });

    addMoreAttributes = await askQuestion('¿Agregar otro atributo? (sí/no): ');
  }

  let addMoreRelations = 'sí';
  while (addMoreRelations.toLowerCase() === 'sí') {
    const relatedEntity = await askQuestion('Entidad relacionada: ');
    const relationType = await askQuestion('Tipo de relación (uno-a-uno, uno-a-muchos, muchos-a-muchos): ');

    relations.push({
      relatedEntity,
      relationType,
    });

    addMoreRelations = await askQuestion('¿Agregar otra relación? (sí/no): ');
  }

  return { name, attributes, relations };
};

const generateEntityCode = (entity: Entity): string => {
  const { name, attributes, relations } = entity;

  const attributesCode = attributes.map(({ name }) => `  ${name}: string;`).join('\n');

  const relationsCode = relations
    .map(({ relatedEntity, relationType }) => `  // Relación: ${relationType} con ${relatedEntity}`)
    .join('\n');

  return `export interface I${name} {
${attributesCode}
${relationsCode}
}

export type New${name} = Omit<I${name}, 'id'> & { id: null };`;
};

(async () => {
  const entity = await createEntity();
  const code = generateEntityCode(entity);

  console.log('\nCódigo generado:\n');
  console.log(code);
  rl.close();
})();
